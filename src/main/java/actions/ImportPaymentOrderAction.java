package actions;

import app.App;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import gui.dialog.ErrorMessageDialog;
import gui.standard.ColumnValue;
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

import static gui.standard.form.misc.QueryBuilder.Query.WhereTypesEnum.EQUALS;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ImportPaymentOrderAction extends AbstractAction {

    private static final String TITLE = "Primi nalog matora";

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;
    private SuperMetaTable paymentOrderMetaTable;
    private SuperMetaTable bankDetailsMetaTable;

    public ImportPaymentOrderAction() {
        super(TITLE);

        this.bankDetailsMetaTable =
                new SuperMetaTable(MosquitoSingletone.getInstance().getMetaTable("BANKA_POSLOVNOG_PARTNERA"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            PaymentOrder paymentOrder = getPaymentOrder();

            String debtorBankCode =
                    paymentOrder.getDebtorAccountDetails().getAccountNumber().substring(0, 3);
            String creditorBankCode =
                    paymentOrder.getCreditorAccountDetails().getAccountNumber().substring(0, 3);

            if (debtorBankCode.equals(creditorBankCode)) {
                transferFunds(paymentOrder);
            } else if (paymentOrder.isUrgent()
                //|| paymentOrder.getAmount().compareTo(new BigDecimal(250000)) > 0 // TODO enable
                    ) {
                rtgs(paymentOrder);

            } else {
                // TODO MAJOR store payment with insert and trigger
            }
        } catch (Exception e1) {
            ErrorMessageDialog.show(App.getMainFrame(), e1);
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

    private void transferFunds(PaymentOrder paymentOrder) {
        BigDecimal amount = paymentOrder.getAmount();

        // TODO provera stanja - exception nema dovoljno para
        // TODO exception racun blokiran

        // TODO MAJOR update database with insert and trigger
    }

    private void rtgs(PaymentOrder paymentOrder) throws SQLException {
        BankDetails debtorBankDetails = getBankDetails(
                paymentOrder.getDebtorAccountDetails().getAccountNumber().substring(0, 3));
        BankDetails creditorBankDetails = getBankDetails(
                paymentOrder.getCreditorAccountDetails().getAccountNumber().substring(0, 3));

        // export
        Mt103 mt103 = mapToMt103(paymentOrder, debtorBankDetails, creditorBankDetails);

        XmlHelper.writeToFile(mt103, "mt103_" + mt103.getMessageId());

        // TODO MAJOR update database
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

    private Mt103 mapToMt103(PaymentOrder paymentOrder, BankDetails debtorBankDetails,
                             BankDetails creditorBankDetails) {
        Mt103 ret = new Mt103();

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

    // builds xml for import
    public static void main(String[] args) {
        PaymentOrder paymentOrder = new PaymentOrder();

        GregorianCalendar cal;

        paymentOrder.setMessageId("777");
        paymentOrder.setDebtor("dejan");
        paymentOrder.setPaymentPurpose("raspberi");
        paymentOrder.setCreditor("dealxtremah");

        cal = new GregorianCalendar();
        cal.setTime(new Date());
        paymentOrder.setOrderDate(new XMLGregorianCalendarImpl(cal));

        cal = new GregorianCalendar();
        cal.setTime(new Date());
        paymentOrder.setCurrencyDate(new XMLGregorianCalendarImpl(cal));

        AccountDetails debtorAccountDetails = new AccountDetails();
        debtorAccountDetails.setAccountNumber("9U4KWP");
        debtorAccountDetails.setModel(38);
        debtorAccountDetails.setReferenceNumber("BQ");
        paymentOrder.setDebtorAccountDetails(debtorAccountDetails);

        AccountDetails creditorAccountDetails = new AccountDetails();
        creditorAccountDetails.setAccountNumber("VW3JXNRZK60AT73");
        creditorAccountDetails.setModel(38);
        creditorAccountDetails.setReferenceNumber("BQ");
        paymentOrder.setCreditorAccountDetails(creditorAccountDetails);

        paymentOrder.setAmount(BigDecimal.TEN);
        paymentOrder.setCurrencyCode("2P");

        XmlHelper.writeToFile(paymentOrder, "paymentOrderForImport");
    }
}
