package actions;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import database.DBConnection;
import gui.dialog.Toast;
import gui.standard.Column;
import gui.standard.form.misc.ProcedureCallFactory;
import gui.standard.form.misc.StatementExecutor;
import messages.QuestionMessages;
import meta.MosquitoSingletone;
import meta.SuperMetaTable;
import xml.XmlHelper;
import xml.cmn.AccountDetails;
import xml.cmn.BankDetails;
import xml.mp.Mt102;
import xml.mp.Mt102Payment;
import xml.mp.ObjectFactory;
import xml.po.PaymentOrder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.CREATE_PROCEDURE_CALL;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ExportClearingAction extends AbstractAction {

    private static final String TITLE = "Salji poruke matora";

    private int i = 0;

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;

    public ExportClearingAction() {
        super(TITLE);

        // TODO
        orderMetaTable = new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("MEDJUBANKARSKI_NALOG"));
        orderItemMetaTable = new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("ANALITIKA_STAVKE"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int ret = JOptionPane.showConfirmDialog(null, "Pokreni slanje mt102 poruka?",
                QuestionMessages.TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (ret == JOptionPane.YES_OPTION) {
            Toast.show("Pokrenuto slanje poruka.");

            new Thread(new Runnable() {
                public void run() {
                    List<PaymentOrder> paymentOrders = createPaymentOrders();
                    List<Mt102> mt102s = createMt102s(paymentOrders);

                    marshallMt102(mt102s.get(0));
//                    try {
//                        saveMt102(mt102s.get(0));
//                    } catch (SQLException e1) {
//                        e1.printStackTrace();
//                    }
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
//                for (Mt102 mt102 : mt102s) {
//                    marshallMt102(mt102);
//                    saveMt102(mt102);
//                }

                    Toast.show("Poruke poslate.");
                }
            }).start();
        }
    }


    private List<PaymentOrder> createPaymentOrders() {
        List<PaymentOrder> ret = null;
        try {
            CallableStatement cs = DBConnection.getConnection().prepareCall("{ call getUnprocessedPayments ()}");
            ResultSet resultSet = cs.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                // TODO no results message - throw exception
            } else {
                ret = new ArrayList<>();

                while (resultSet.next()) {
                    PaymentOrder paymentOrder = new PaymentOrder();

                    GregorianCalendar cal;

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
            e.printStackTrace();
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
            // TODO get bank details, cache in map

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

            mt102.setCreditorBankDetails(creditorBankDetails);
            mt102.setDebtorBankDetails(debtorBankDetails);
            mt102.setMessageId(Integer.toString(i++)); // TODO set message id
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

    private void marshallMt102(Mt102 mt102) {
        String marshalledMt102 = XmlHelper.marshall(mt102);

        try {
            File file = new File("src/main/resources/export/mt102_" + mt102.getMessageId() + ".xml");
            if (!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(marshalledMt102);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMt102(Mt102 mt102) throws SQLException {
        // TODO save to db
        // save mb nalog
        StatementExecutor orderExecutor = new StatementExecutor(orderMetaTable.getBaseColumnTypes());

        List<Column> values = new ArrayList<>();

        Iterator<String> it = orderMetaTable.getBaseColumnCodes().iterator();

        values.add(new Column(it.next(), mt102.getMessageId()));
        // TODO change
        values.add(new Column(it.next(), Integer.toString(301)));
        values.add(new Column(it.next(), Integer.toString(410)));
        values.add(new Column(it.next(), mt102.getTotalAmount().toString()));
        values.add(new Column(it.next(), mt102.getDate().toString()));// TODO format?
        values.add(new Column(it.next(), Integer.toString(0)));
        values.add(new Column(it.next(), Integer.toString(1))); // TODO real value

        orderExecutor.executeProcedure(ProcedureCallFactory.getProcedureCall("MEDJUBANKARSKI_NALOG",
                CREATE_PROCEDURE_CALL), values);

        int i = 1;
        for (Mt102Payment mt102Payment : mt102.getPayments().getPayment()) {
            //save analitika stavke
            StatementExecutor orderItemExecutor = new StatementExecutor(orderItemMetaTable.getBaseColumnTypes());

            values = new ArrayList<>();
            it = orderItemMetaTable.getBaseColumnCodes().iterator();

            values.add(new Column(it.next(), Integer.toString(i++)));
            values.add(new Column(it.next(), mt102Payment.getDebtorAccountDetails().getAccountNumber()));
            values.add(new Column(it.next(), mt102Payment.getPaymentOrderId()));
            values.add(new Column(it.next(), mt102.getDate().toString()));// TODO format?
            values.add(new Column(it.next(), Integer.toString(0)));
            values.add(new Column(it.next(), Integer.toString(1))); // TODO real value

            orderItemExecutor.executeProcedure(ProcedureCallFactory.getProcedureCall("ANALITIKA_STAVKE",
                    CREATE_PROCEDURE_CALL), values);
        }
    }
}