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
import gui.standard.form.misc.QueryBuilder;
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
import static gui.standard.form.misc.QueryBuilder.Query.WhereTypesEnum.EQUALS;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ExportClearingAction extends AbstractAction {

    private static final String TITLE = "Salji poruke matora";

    private int i = 1;

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;
    private SuperMetaTable paymentOrderMetaTable;
    private SuperMetaTable bankDetailsMetaTable;

    public ExportClearingAction() {
        super(TITLE);

        this.orderMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("MEDJUBANKARSKI_NALOG"));
        this.orderItemMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("ANALITIKA_STAVKE"));
        this.paymentOrderMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("ANALITIKA_IZVODA"));
        this.bankDetailsMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("BANKA_POSLOVNOG_PARTNERA"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int ret = JOptionPane.showConfirmDialog(null, "Pokreni slanje mt102 poruka?",
                QuestionMessages.TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            Toast.show(App.getMainFrame(), "Pokrenuto slanje poruka.");

            new Thread(new Runnable() {
                public void run() {
                    Map<Integer, Map<String, String>> poHashPoPkValues = new HashMap<>();

                    List<PaymentOrder> paymentOrders = createPaymentOrders(poHashPoPkValues);

                    if (paymentOrders.size() == 0)
                        Toast.show(App.getMainFrame(), "Nema poruka za slanje.");
                    else {
                        Map<Integer, Mt102> poHashMt102Map = new HashMap<>();

                        List<Mt102> mt102s = createMt102s(paymentOrders, poHashMt102Map);

                        try {
                            for (Mt102 mt102 : mt102s) {
                                XmlHelper.writeToFile(mt102, "mt102_" + mt102.getMessageId());

                                saveClearingOrder(mt102);
                            }

                            for (Integer poHash : poHashPoPkValues.keySet())
                                saveClearingOrderItem(i++, poHashMt102Map.get(poHash), poHashPoPkValues.get(poHash));


                            for (PaymentOrder po : paymentOrders)
                                updatePaymentOrderStatus(poHashPoPkValues.get(po.hashCode()));
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                        Toast.show(App.getMainFrame(), "Poruke poslate.");
                    }
                }
            }).start();
        }
    }


    private List<PaymentOrder> createPaymentOrders(Map<Integer, Map<String, String>> poHashPoPkValues) {
        List<PaymentOrder> ret = null;
        try {
            CallableStatement statement =
                    DBConnection.getConnection().prepareCall("{ call getUnprocessedPayments (?)}");
            statement.setString(1, AppData.getInstance().getValue(PIB_BANKE));
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                throw new SQLException("Nema podataka za export.", null, ErrorMessages.CUSTOM_CODE);
            } else {
                ret = new ArrayList<>();

                while (resultSet.next()) {
                    Map<String, String> pkValues = new HashMap<>();
                    PaymentOrder paymentOrder = new PaymentOrder();

                    GregorianCalendar cal;
                    AccountDetails debtorAccountDetails;
                    AccountDetails creditorAccountDetails;

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

                    debtorAccountDetails = new AccountDetails();
                    debtorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACDUZ"));
                    debtorAccountDetails.setModel(resultSet.getInt("ASI_MODZAD"));
                    debtorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBZAD"));
                    paymentOrder.setDebtorAccountDetails(debtorAccountDetails);

                    creditorAccountDetails = new AccountDetails();
                    creditorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACPOV"));
                    creditorAccountDetails.setModel(resultSet.getInt("ASI_MODODOB"));
                    creditorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBODO"));
                    paymentOrder.setCreditorAccountDetails(creditorAccountDetails);

                    paymentOrder.setAmount(resultSet.getBigDecimal("ASI_IZNOS"));
                    paymentOrder.setCurrencyCode(resultSet.getString("VA_IFRA"));

                    ret.add(paymentOrder);

                    pkValues.put("PR_PIB", resultSet.getString("PR_PIB"));
                    pkValues.put("BAR_RACUN", resultSet.getString("BAR_RACUN"));
                    pkValues.put("DSR_IZVOD", resultSet.getString("DSR_IZVOD"));
                    pkValues.put("ASI_BROJSTAVKE", resultSet.getString("ASI_BROJSTAVKE"));
                    poHashPoPkValues.put(paymentOrder.hashCode(), pkValues);
                }
            }
        } catch (SQLException e) {
            ErrorMessageDialog.show(App.getMainFrame(), e);
        }

        return ret;
    }

    private List<Mt102> createMt102s(List<PaymentOrder> paymentOrders, Map<Integer, Mt102> poHashCodeMt102Map) {
        List<Mt102> ret = new ArrayList<>();

        Map<String, List<PaymentOrder>> paymentOrdersByBank = new HashMap<>();

        for (PaymentOrder paymentOrder : paymentOrders) {
            String creditorBankCode = paymentOrder.getCreditorAccountDetails().getAccountNumber().substring(0, 3);

            if (!paymentOrdersByBank.containsKey(creditorBankCode)) {
                paymentOrdersByBank.put(creditorBankCode, new ArrayList<PaymentOrder>());
                paymentOrdersByBank.get(creditorBankCode).add(paymentOrder);
            } else {
                paymentOrdersByBank.get(creditorBankCode).add(paymentOrder);
            }
        }

        for (List<PaymentOrder> pos : paymentOrdersByBank.values()) {
            String debtorBankCode = pos.get(0).getDebtorAccountDetails().getAccountNumber().substring(0, 3);
            String creditorBankCode = pos.get(0).getDebtorAccountDetails().getAccountNumber().substring(0, 3);

            try {
                BankDetails debtorBankDetails = getBankDetails(debtorBankCode);
                BankDetails creditorBankDetails = getBankDetails(creditorBankCode);

                ret.add(createMt102(pos, debtorBankDetails, creditorBankDetails));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (PaymentOrder po : pos) {
                poHashCodeMt102Map.put(po.hashCode(), ret.get(ret.size() - 1));
            }
        }

        return ret;
    }

    private BankDetails getBankDetails(String bankCode) throws SQLException {
        BankDetails ret = null;

        StatementExecutor executor = new StatementExecutor(bankDetailsMetaTable.getPkColumnTypes());
        List<ColumnValue> values = new ArrayList<>();
        List<String[]> results = null;

        values.add(new ColumnValue("SIFRA_BANKE", bankCode));

        results = executor.execute(new QueryBuilder.Query("SELECT * FROM BANKA_POSLOVNOG_PARTNERA " +
                "WHERE SIFRA_BANKE=?", EQUALS), values, bankDetailsMetaTable.getBaseColumnCodes());

        if (results.isEmpty()) {
            throw new SQLException("Nema banke u šifarniku banaka.", null, ErrorMessages.CUSTOM_CODE);
        } else {
            ret = new BankDetails();
            ret.setSwiftCode(results.get(0)[0]);
            ret.setBankClearingAccountNumber(results.get(0)[1]);
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

        String debtorBankCode = mt102.getPayments().getPayment().get(0).getDebtorAccountDetails()
                .getAccountNumber().substring(0, 3);
        String creditorBankCode = mt102.getPayments().getPayment().get(0).getCreditorAccountDetails()
                .getAccountNumber().substring(0, 3);

        Iterator<String> it = orderMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), mt102.getMessageId()));
        values.add(new ColumnValue(it.next(), debtorBankCode));
        values.add(new ColumnValue(it.next(), creditorBankCode));
        // TODO update table to support larger amount
        values.add(new ColumnValue(it.next(), mt102.getPayments().getPayment().get(0).getAmount()));
        values.add(new ColumnValue(it.next(), mt102.getDate()));
        values.add(new ColumnValue(it.next(), false));
        values.add(new ColumnValue(it.next(), 1));

        orderExecutor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("MEDJUBANKARSKI_NALOG",
                values.size()), values);
    }

    private void saveClearingOrderItem(int j, Mt102 mt102, Map<String, String> foo)
            throws SQLException {
        StatementExecutor orderItemExecutor = new StatementExecutor(orderItemMetaTable.getBaseColumnTypes());

        List<ColumnValue> values = new ArrayList<>();
        Iterator<String> it = orderItemMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), j)); // TODO autoincrement?
        values.add(new ColumnValue(it.next(), mt102.getMessageId()));
        values.add(new ColumnValue(it.next(), foo.get("PR_PIB")));
        values.add(new ColumnValue(it.next(), foo.get("BAR_RACUN")));
        values.add(new ColumnValue(it.next(), foo.get("DSR_IZVOD")));
        values.add(new ColumnValue(it.next(), foo.get("ASI_BROJSTAVKE")));

        orderItemExecutor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("ANALITIKA_STAVKE",
                values.size()), values);
    }

    private void updatePaymentOrderStatus(Map<String, String> pkValues) throws SQLException {
        Map<String, String> columnTypes = paymentOrderMetaTable.getPkColumnTypes();
        columnTypes.put("ASI_STATUS", "java.lang.String");

        StatementExecutor orderItemExecutor = new StatementExecutor(columnTypes);

        List<ColumnValue> values = new ArrayList<>();
        List<String> columnCodes = paymentOrderMetaTable.getPkColumnCodes();
        columnCodes.add("ASI_STATUS");

        Iterator<String> it = columnCodes.iterator();

        values.add(new ColumnValue(it.next(), pkValues.get("PR_PIB")));
        values.add(new ColumnValue(it.next(), pkValues.get("BAR_RACUN")));
        values.add(new ColumnValue(it.next(), pkValues.get("DSR_IZVOD")));
        values.add(new ColumnValue(it.next(), pkValues.get("ASI_BROJSTAVKE")));
        values.add(new ColumnValue(it.next(), "P"));

        orderItemExecutor.executeProcedure("{ call updatePaymentStatus(?,?,?,?,?)}", values);
    }
}