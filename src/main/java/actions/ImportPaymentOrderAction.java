package actions;

import app.App;
import app.AppData;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import gui.dialog.ErrorMessageDialog;
import gui.dialog.Toast;
import gui.standard.ColumnValue;
import gui.standard.form.misc.ProcedureCallFactory;
import gui.standard.form.misc.QueryBuilder;
import gui.standard.form.misc.StatementExecutor;
import messages.ErrorMessages;
import meta.MosquitoSingletone;
import meta.SuperMetaTable;
import xml.XmlHelper;
import xml.cmn.AccountDetails;
import xml.cmn.BankDetails;
import xml.mp.Mt103;
import xml.po.PaymentOrder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import static app.AppData.AppDataEnum.PIB_BANKE;
import static gui.standard.form.misc.QueryBuilder.Query.WhereTypesEnum.EQUALS;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ImportPaymentOrderAction extends AbstractAction {

    private static final String TITLE = "Primi nalog matora";

    private enum Status {
        E, P, I
    }

    int i = 888;

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;
    private SuperMetaTable paymentOrderMetaTable;
    private SuperMetaTable bankDetailsMetaTable;

    public ImportPaymentOrderAction() {
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
        try {
            PaymentOrder paymentOrder = getPaymentOrder();

            if(paymentOrder != null) {
                String debtorBankCode =
                        paymentOrder.getDebtorAccountDetails().getAccountNumber().substring(0, 3);
                String creditorBankCode =
                        paymentOrder.getCreditorAccountDetails().getAccountNumber().substring(0, 3);

                if (debtorBankCode.equals(creditorBankCode)) {
                    insertPaymentOrder(paymentOrder, true, Status.I);
                    insertPaymentOrder(paymentOrder, false, Status.I);

                    Toast.show(App.getMainFrame(), "Nalog primljen i izvršen prenos novca.");
                } else if (paymentOrder.isUrgent()
                        || paymentOrder.getAmount().compareTo(new BigDecimal(250000)) > 0) {
                    Mt103 mt103;

                    insertPaymentOrder(paymentOrder, true, Status.P);

                    mt103 = getMt103(paymentOrder);

                    XmlHelper.writeToFile(mt103, "mt103_" + mt103.getMessageId());
                    saveMt103(mt103);
                    saveOrderItem(mt103, paymentOrder.getMessageId());

                    Toast.show(App.getMainFrame(), "Nalog primljen i poslata MT103 poruka.");
                } else {
                    insertPaymentOrder(paymentOrder, true, Status.E);

                    Toast.show(App.getMainFrame(), "Nalog primljen.");
                }
            }
        } catch (Exception e1) {
            ErrorMessageDialog.show(App.getMainFrame(), e1);
            e1.printStackTrace();
        }
    }

    private PaymentOrder getPaymentOrder() throws Exception {
        PaymentOrder ret = null;

        JFileChooser openDialog = new JFileChooser();
        openDialog.setFileFilter(new FileNameExtensionFilter(
                "XML File(*.xml)", "xml"));

        int openDialogRetVal = openDialog.showOpenDialog(null);

        if (openDialogRetVal == JFileChooser.APPROVE_OPTION) {
            File file = openDialog.getSelectedFile();
            String path = file.getPath();

            InputStream is = new FileInputStream(new File(path));

            ret = XmlHelper.unmarshall(is, PaymentOrder.class);
        }

        return ret;
    }

    private void insertPaymentOrder(PaymentOrder paymentOrder, boolean debtorsPayment, Status status) {
        BigDecimal amount = paymentOrder.getAmount();

        try {
            StatementExecutor executor = new StatementExecutor(paymentOrderMetaTable.getBaseColumnTypes());

            List<ColumnValue> values = new ArrayList<>();
            Iterator<String> it = paymentOrderMetaTable.getBaseColumnCodes().iterator();

            Date endDate;
            int statementNumber = 0;
            String statusValue;

            values.add(new ColumnValue(it.next(), AppData.getInstance().getValue(PIB_BANKE)));
            if (debtorsPayment)
                values.add(new ColumnValue(it.next(), paymentOrder.getDebtorAccountDetails().getAccountNumber()));
            else
                values.add(new ColumnValue(it.next(), paymentOrder.getCreditorAccountDetails().getAccountNumber()));
            values.add(new ColumnValue(it.next(), statementNumber));
            it.next(); // skip ASI_BROJSTAVKE
            values.add(new ColumnValue(it.next(), paymentOrder.getCurrencyCode()));
            values.add(new ColumnValue(it.next(), "1"));
            values.add(new ColumnValue(it.next(), "A"));
            values.add(new ColumnValue(it.next(), paymentOrder.getMessageId()));

            values.add(new ColumnValue(it.next(), paymentOrder.getDebtor()));
            values.add(new ColumnValue(it.next(), paymentOrder.getPaymentPurpose()));
            values.add(new ColumnValue(it.next(), paymentOrder.getCreditor()));

            endDate = new Date();
            values.add(new ColumnValue(it.next(), new java.sql.Date(endDate.getTime())));
            values.add(new ColumnValue(it.next(), paymentOrder.getCurrencyDate()));

            values.add(new ColumnValue(it.next(), paymentOrder.getDebtorAccountDetails().getAccountNumber()));
            values.add(new ColumnValue(it.next(), paymentOrder.getDebtorAccountDetails().getModel()));
            values.add(new ColumnValue(it.next(), paymentOrder.getDebtorAccountDetails().getReferenceNumber()));

            values.add(new ColumnValue(it.next(), paymentOrder.getCreditorAccountDetails().getAccountNumber()));
            values.add(new ColumnValue(it.next(), paymentOrder.getCreditorAccountDetails().getModel()));
            values.add(new ColumnValue(it.next(), paymentOrder.getCreditorAccountDetails().getReferenceNumber()));

            values.add(new ColumnValue(it.next(), false));
            values.add(new ColumnValue(it.next(), amount));
            values.add(new ColumnValue(it.next(), 1));

            switch (status) {
                default:
                case E:
                    statusValue = "E";
                    break;
                case P:
                    statusValue = "P";
                    break;
                case I:
                    statusValue = "I";
                    break;
            }
            values.add(new ColumnValue(it.next(), statusValue));

            executor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("ANALITIKA_IZVODA", values.size()),
                    values);
        } catch (SQLException e) {
            ErrorMessageDialog.show(App.getMainFrame(), e);
        }
    }

    private Mt103 getMt103(PaymentOrder paymentOrder) throws SQLException {
        BankDetails debtorBankDetails = getBankDetails(
                paymentOrder.getDebtorAccountDetails().getAccountNumber().substring(0, 3));
        BankDetails creditorBankDetails = getBankDetails(
                paymentOrder.getCreditorAccountDetails().getAccountNumber().substring(0, 3));

        return mapToMt103(paymentOrder, debtorBankDetails, creditorBankDetails);
    }

    private BankDetails getBankDetails(String bankCode) throws SQLException {
        BankDetails ret;

        StatementExecutor executor = new StatementExecutor(bankDetailsMetaTable.getPkColumnTypes());
        List<ColumnValue> values = new ArrayList<>();
        List<String[]> results;

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

    private Mt103 mapToMt103(PaymentOrder paymentOrder, BankDetails debtorBankDetails,
                             BankDetails creditorBankDetails) {
        Mt103 ret = new Mt103();

        ret.setMessageId(Integer.toString(i++));
        ret.setDebtorBankDetails(debtorBankDetails);
        ret.setCreditorBankDetails(creditorBankDetails);
        ret.setDebtor(paymentOrder.getDebtor());
        ret.setPaymentPurpose(paymentOrder.getPaymentPurpose());
        ret.setCreditor(paymentOrder.getCreditor());
        ret.setOrderDate(paymentOrder.getOrderDate());
        ret.setDebtorAccountDetails(paymentOrder.getDebtorAccountDetails());
        ret.setCreditorAccountDetails(paymentOrder.getCreditorAccountDetails());
        ret.setAmount(paymentOrder.getAmount());
        ret.setCurrencyDate(paymentOrder.getCurrencyDate());
        ret.setCurrencyCode(paymentOrder.getCurrencyCode());

        return ret;
    }

    private void saveMt103(Mt103 mt103) throws SQLException {
        StatementExecutor orderExecutor = new StatementExecutor(orderMetaTable.getBaseColumnTypes());

        List<ColumnValue> values = new ArrayList<>();

        String debtorBankCode = mt103.getDebtorAccountDetails().getAccountNumber().substring(0, 3);
        String creditorBankCode = mt103.getCreditorAccountDetails().getAccountNumber().substring(0, 3);

        Iterator<String> it = orderMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), mt103.getMessageId()));
        values.add(new ColumnValue(it.next(), debtorBankCode));
        values.add(new ColumnValue(it.next(), creditorBankCode));
        // TODO update table to support larger amount
        values.add(new ColumnValue(it.next(), mt103.getAmount()));
        values.add(new ColumnValue(it.next(), mt103.getOrderDate()));
        values.add(new ColumnValue(it.next(), true));
        values.add(new ColumnValue(it.next(), 1));

        orderExecutor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("MEDJUBANKARSKI_NALOG",
                values.size()), values);
    }

    private void saveOrderItem(Mt103 mt103, String paymentOrderId)
            throws SQLException {
        StatementExecutor executor = new StatementExecutor(orderItemMetaTable.getBaseColumnTypes());

        String q = "SELECT PR_PIB, BAR_RACUN, DSR_IZVOD, ASI_BROJSTAVKE " +
                "FROM ANALITIKA_IZVODA " +
                "WHERE ID_NALOGA_PL=" + paymentOrderId;

        List<String> resultColumnCodes = new ArrayList<>();
        resultColumnCodes.add("PR_PIB");
        resultColumnCodes.add("BAR_RACUN");
        resultColumnCodes.add("DSR_IZVOD");
        resultColumnCodes.add("ASI_BROJSTAVKE");

        String[] results = executor.execute(q, resultColumnCodes).get(0);

        List<ColumnValue> values = new ArrayList<>();
        Iterator<String> it = orderItemMetaTable.getBaseColumnCodes().iterator();

        values.add(new ColumnValue(it.next(), 0)); // TODO autoincrement?
        values.add(new ColumnValue(it.next(), mt103.getMessageId()));
        values.add(new ColumnValue(it.next(), results[0]));
        values.add(new ColumnValue(it.next(), results[1]));
        values.add(new ColumnValue(it.next(), results[2]));
        values.add(new ColumnValue(it.next(), results[3]));

        executor.executeProcedure(ProcedureCallFactory.getCreateProcedureCall("ANALITIKA_STAVKE", values.size()),
                values);
    }
}
