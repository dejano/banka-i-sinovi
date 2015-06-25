package actions;

import app.App;
import app.AppData;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import database.DBConnection;
import gui.MainFrame;
import gui.dialog.ErrorMessageDialog;
import gui.dialog.Toast;
import gui.standard.ColumnValue;
import gui.standard.form.misc.ProcedureCallFactory;
import gui.standard.form.misc.StatementExecutor;
import messages.ErrorMessages;
import messages.QuestionMessages;
import meta.MosquitoSingletone;
import meta.SuperMetaTable;
import sun.applet.Main;
import xml.XmlHelper;
import xml.cmn.AccountDetails;
import xml.cmn.BankDetails;
import xml.mp.Mt102;
import xml.mp.Mt102Payment;
import xml.mp.ObjectFactory;
import xml.po.PaymentOrder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static app.AppData.AppDataEnum.PIB_BANKE;
import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.CREATE_PROCEDURE_CALL;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ExportClearingAction extends AbstractAction {

    private static final String TITLE = "Salji poruke matora";

    private int i = 0;

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;
    private SuperMetaTable paymentOrderMetaTable;

    public ExportClearingAction() {
        super(TITLE);

        this.orderMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("MEDJUBANKARSKI_NALOG"));
        this.orderItemMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("ANALITIKA_STAVKE"));
        this.paymentOrderMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("ANALITIKA_IZVODA"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int ret = JOptionPane.showConfirmDialog(null, "Pokreni slanje mt102 poruka?",
                QuestionMessages.TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            Toast.show(App.getMainFrame(), "Pokrenuto slanje poruka.");

            new Thread(new Runnable() {
                public void run() {
                    List<PaymentOrder> paymentOrders = createPaymentOrders();
                    List<Mt102> mt102s = createMt102s(paymentOrders);

                    try {
                        for (Mt102 mt102 : mt102s) {
                            XmlHelper.writeToFile(mt102, "mt102_" + mt102.getMessageId());

                            saveClearingOrder(mt102);
                            for (Mt102Payment mt102Payment : mt102.getPayments().getPayment()) {
                                saveClearingOrderItem(mt102, mt102Payment);
                            }

                            for (PaymentOrder paymentOrder : paymentOrders)
                                updatePaymentOrderStatus(paymentOrder);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    Toast.show(App.getMainFrame(), "Poruke poslate.");
                }
            }).start();
        }
    }


    private List<PaymentOrder> createPaymentOrders() {
        List<PaymentOrder> ret = null;
        try {
            CallableStatement cs = DBConnection.getConnection().prepareCall("{ call getUnprocessedPayments (?)}");
            cs.setString(1, AppData.getInstance().getValue(PIB_BANKE));
            ResultSet resultSet = cs.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                throw new SQLException("Nema podataka za export.", null, ErrorMessages.CUSTOM_CODE);
            } else {
                ret = new ArrayList<>();

                while (resultSet.next()) {
                    PaymentOrder paymentOrder = new PaymentOrder();

                    GregorianCalendar cal;

                    paymentOrder.setMessageId(resultSet.getString("ASI_BROJSTAVKE"));
                    paymentOrder.setDebtor(resultSet.getString("ASI_DUZNIK"));
                    paymentOrder.setPaymentPurpose(resultSet.getString("ASI_SVRHA"));
                    paymentOrder.setCreditor(resultSet.getString("ASI_POVERILAC"));

                    cal = new GregorianCalendar();
                    cal.setTime(resultSet.getDate("ASI_DATPRI"));
                    paymentOrder.setOrderDate(new XMLGregorianCalendarImpl(cal));

                    cal = new GregorianCalendar();
                    cal.setTime(resultSet.getDate("ASI_DATVAL"));
                    paymentOrder.setCurrencyDate(new XMLGregorianCalendarImpl(cal));

                    AccountDetails debtorAccountDetails = new AccountDetails();
                    debtorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACDUZ"));
                    debtorAccountDetails.setModel(resultSet.getInt("ASI_MODZAD"));
                    debtorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBZAD"));
                    paymentOrder.setDebtorAccountDetails(debtorAccountDetails);

                    AccountDetails creditorAccountDetails = new AccountDetails();
                    creditorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACPOV"));
                    creditorAccountDetails.setModel(resultSet.getInt("ASI_MODODOB"));
                    creditorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBODO"));
                    paymentOrder.setCreditorAccountDetails(creditorAccountDetails);

                    paymentOrder.setAmount(resultSet.getBigDecimal("ASI_IZNOS"));
                    paymentOrder.setCurrencyCode(resultSet.getString("VA_IFRA"));

                    ret.add(paymentOrder);
                }
            }
        } catch (SQLException e) {
            ErrorMessageDialog.show(App.getMainFrame(), e);
        }

        return ret;
    }

    private List<Mt102> createMt102s(List<PaymentOrder> paymentOrders) {
        List<Mt102> ret = new ArrayList<>();

        Map<String, List<PaymentOrder>> paymentOrdersByBank = new HashMap<>();

        for (PaymentOrder paymentOrder : paymentOrders) {
            String creditorBankCode = paymentOrder.getCreditorAccountDetails().getAccountNumber();//.substring(0, 3); TODO

            if (!paymentOrdersByBank.containsKey(creditorBankCode)) {
                paymentOrdersByBank.put(creditorBankCode, new ArrayList<PaymentOrder>());
                paymentOrdersByBank.get(creditorBankCode).add(paymentOrder);
            } else {
                paymentOrdersByBank.get(creditorBankCode).add(paymentOrder);
            }
        }

        for (List<PaymentOrder> pos : paymentOrdersByBank.values()) {
            // TODO get bank details, cache in mapBoolean

            ret.add(createMt102(pos, null, null));
        }

        return ret;
    }


    private Mt102 createMt102(List<PaymentOrder> paymentOrders, BankDetails creditorBankDetails,
                              BankDetails debtorBankDetails) {
        Mt102 mt102 = null;

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (paymentOrders.size() > 0) {
            ObjectFactory objectFactory = new ObjectFactory();
            mt102 = objectFactory.createMt102();
            mt102.setPayments(objectFactory.createMt102Payments());

            mt102.setMessageId(Integer.toString(i++)); // TODO set message id
            mt102.setCreditorBankDetails(creditorBankDetails);
            mt102.setDebtorBankDetails(debtorBankDetails);
            mt102.setCurrencyCode(paymentOrders.get(0).getCurrencyCode());
            mt102.setCurrencyDate(paymentOrders.get(0).getCurrencyDate());
            mt102.setDate(new XMLGregorianCalendarImpl(new GregorianCalendar()));

            for (PaymentOrder paymentOrder : paymentOrders) {
                totalAmount = totalAmount.add(paymentOrder.getAmount());

                Mt102Payment mt102payment = mapToMt102Payment(paymentOrder);
                mt102.getPayments().getPayment().add(mt102payment);
            }

            mt102.setTotalAmount(totalAmount);
        }

        return mt102;
    }

    private Mt102Payment mapToMt102Payment(PaymentOrder paymentOrder) {
        Mt102Payment mt102Payment = new Mt102Payment();

        mt102Payment.setAmount(paymentOrder.getAmount());
        mt102Payment.setCreditor(paymentOrder.getCreditor());
        mt102Payment.setCreditorAccountDetails(paymentOrder.getCreditorAccountDetails());
        mt102Payment.setDebtor(paymentOrder.getDebtor());
        mt102Payment.setDebtorAccountDetails(paymentOrder.getDebtorAccountDetails());
        mt102Payment.setOrderDate(paymentOrder.getOrderDate());
        mt102Payment.setPaymentPurpose(paymentOrder.getPaymentPurpose());
        mt102Payment.setCurrencyCode(paymentOrder.getCurrencyCode());
        mt102Payment.setPaymentOrderId(paymentOrder.getMessageId());

        return mt102Payment;
    }



    private void saveClearingOrder(Mt102 mt102) throws SQLException {
        StatementExecutor orderExecutor = new StatementExecutor(orderMetaTable.getBaseColumnTypes());

        List<ColumnValue> values = new ArrayList<>();

        Iterator<String> it = orderMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), mt102.getMessageId()));
        values.add(new ColumnValue(it.next(), 838)); // TODO real value
        values.add(new ColumnValue(it.next(), 410));
        // TODO update table to support larger amount
        values.add(new ColumnValue(it.next(), mt102.getPayments().getPayment().get(0).getAmount()));
        values.add(new ColumnValue(it.next(), mt102.getDate()));
        values.add(new ColumnValue(it.next(), false)); // TODO real value
        values.add(new ColumnValue(it.next(), 1));

        orderExecutor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("MEDJUBANKARSKI_NALOG",
                values.size()), values);
    }

    private void saveClearingOrderItem(Mt102 mt102, Mt102Payment mt102Payment) throws SQLException {
        StatementExecutor orderItemExecutor = new StatementExecutor(orderItemMetaTable.getBaseColumnTypes());

        List<ColumnValue> values = new ArrayList<>();
        Iterator<String> it = orderItemMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), i++));
        values.add(new ColumnValue(it.next(), mt102Payment.getDebtorAccountDetails().getAccountNumber()));
        values.add(new ColumnValue(it.next(), mt102Payment.getPaymentOrderId()));
        values.add(new ColumnValue(it.next(), mt102.getDate()));
        values.add(new ColumnValue(it.next(), 0));
        values.add(new ColumnValue(it.next(), 1)); // TODO real value

        orderItemExecutor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("ANALITIKA_STAVKE",
                values.size()), values);
    }

    private void updatePaymentOrderStatus(PaymentOrder paymentOrder) throws SQLException {
        Map<String, String> columnTypes = paymentOrderMetaTable.getPkColumnTypes();
        columnTypes.put("ASI_STATUS", "java.lang.String");

        StatementExecutor orderItemExecutor = new StatementExecutor(columnTypes);

        List<ColumnValue> values = new ArrayList<>();
        List<String> columnCodes = paymentOrderMetaTable.getPkColumnCodes();
        columnCodes.add("ASI_STATUS");
        Iterator<String> it = columnCodes.iterator();

        values.add(new ColumnValue(it.next(), paymentOrder.getDebtorAccountDetails().getAccountNumber()));
        values.add(new ColumnValue(it.next(), 340)); // TODO use valid value
        values.add(new ColumnValue(it.next(), paymentOrder.getMessageId()));
        values.add(new ColumnValue(it.next(), "P"));

        orderItemExecutor.executeProcedure("{ call updateAnalitikaIzvodaStatus(?,?,?,?)}", values);
    }
}