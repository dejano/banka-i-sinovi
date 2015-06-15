package actions;

import xml.XmlHelper;
import xml.po.PaymentOrder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Nikola on 14.6.2015..
 */
public class ImportPaymentOrderAction extends AbstractAction {

    private static final String TITLE = "Primi nalog matora";

    public ImportPaymentOrderAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO show file select, read xml

        getPaymentOrder();
    }

    private PaymentOrder getPaymentOrder() {
        PaymentOrder ret = null;

        JFileChooser openDialog = new JFileChooser();
        openDialog.setFileFilter(new FileNameExtensionFilter(
                "XML File(*.xml)", "xml"));

        int openDialogRetVal = openDialog.showOpenDialog(null);

        if (openDialogRetVal == JFileChooser.APPROVE_OPTION) {
            File file = openDialog.getSelectedFile();
            String path = file.getPath();

            try {
                InputStream is = new FileInputStream(new File(path));

                ret = XmlHelper.unmarshall(is, PaymentOrder.class);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return ret;
    }

//    public void paymentOrderHandle(PaymentOrder paymentOrder) {
//        if (BankUtil.areInTheSameBank(paymentOrderPart
//                .getDebtorAccountDetails().getAccountNumber(), paymentOrderPart
//                .getCreditorAccountDetails().getAccountNumber())) {
//            // TODO provera da li postoje racuni
//
//            transferFunds(paymentOrderPart);
//        } else if (paymentOrderPart.isUrgent()
//                || paymentOrderPart.getAmount().compareTo(
//                new BigDecimal(250000)) > 0) {
//            rtgs(paymentOrderPart);
//        }
//    }

//    private void transferFunds(PaymentOrder paymentOrder) {
//        BigDecimal amount = paymentOrder.getAmount();
//
//        String debtorAccountNumber = paymentOrder.getDebtorAccountDetails()
//                .getAccountNumber();
//        BigDecimal debtorBalance = CompanyDataDao
//                .getCompanyBalance(debtorAccountNumber);
//        // TODO provera stanja - exception nema dovoljno para
//        // TODO exception racun blokiran
//
//        String creditorAccountNumber = paymentOrder.getCreditorAccountDetails()
//                .getAccountNumber();
//        BigDecimal creditorBalance = CompanyDataDao
//                .getCompanyBalance(creditorAccountNumber);
//
//        // transfer funds
//        CompanyDataDao.updateCompanyBalance(debtorAccountNumber,
//                debtorBalance.subtract(amount));
//        CompanyDataDao.updateCompanyBalance(creditorAccountNumber,
//                creditorBalance.add(amount));
//    }

//    private void rtgs(PaymentOrder paymentOrder) {
//        String debtorAccountNumber = paymentOrder.getDebtorAccountDetails().getAccountNumber();
//        String creditorAccountNumber = paymentOrder.getCreditorAccountDetails().getAccountNumber();
//
//        BankDetails debtorBankDetails = BdDocumentClient.getBankDetails(BankUtil.getBankCode(debtorAccountNumber));
//        BankDetails creditorBankDetails = BdDocumentClient.getBankDetails(BankUtil.getBankCode(creditorAccountNumber));
//
//        Mt103 mt103 = ObjectMapper.getMt103(paymentOrder, debtorBankDetails, creditorBankDetails);
//
//        Mt900 rtgsResponse = MpcbDocumentClient.sendRtgsRequest(mt103);
//
//        BigDecimal amount = paymentOrder.getAmount();
//
//        // update debtor balance
//        BigDecimal balance = CompanyDataDao
//                .getCompanyBalance(debtorAccountNumber);
//        CompanyDataDao.updateCompanyBalance(debtorAccountNumber,
//                balance.subtract(amount));

//			BigDecimal reservedAmount = CompanyDataDao
//					.getCompanyReservedAmount(debtorAccountNumber);
//			CompanyDataDao.updateCompanyReservedAmount(debtorAccountNumber,
//					reservedAmount.subtract(amount));
//    }
}
