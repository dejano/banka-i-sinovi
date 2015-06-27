package actions;

import app.App;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import database.DBConnection;
import gui.dialog.Toast;
import gui.standard.form.Form;
import gui.standard.form.components.ValidationDatePicker;
import net.miginfocom.swing.MigLayout;
import xml.XmlHelper;
import xml.bs.AccountDetails;
import xml.bs.Statement;
import xml.bs.StatementItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static gui.standard.form.components.ValidationDatePicker.E_VALID_DATES.BEFORE_INCLUDING;

public class ExportStatementAction extends AbstractAction {

    private static final String TITLE = "Eksport izvoda";
    private static final String INPUT_LABEL = "Unesite datum: ";
    private final String HEADER_QUERY = "SELECT *  FROM PRENOS_IZVODA___PRESEK  " +
            "WHERE BAR_RACUN = ?  AND convert(varchar,BNP_DATUM,110)=convert(varchar,?,110)  AND BNP_PRESEK = ?;";

    private final String ITEM_QUERY = "SELECT PRE_PR_PIB, PRE_BAR_RACUN, p.DSR_IZVOD, BNP_DATUM," +
            " BNP_PRESEK, APR_RBR, p.PR_PIB, p.BAR_RACUN, ANA_DSR_IZVOD, p.ASI_BROJSTAVKE, VA_IFRA, " +
            "NM_SIFRA, VPL_OZNAKA, ID_NALOGA_PL, ASI_DUZNIK, ASI_SVRHA, ASI_POVERILAC, ASI_DATPRI, " +
            "ASI_DATVAL, ASI_RACDUZ, ASI_MODZAD, ASI_PBZAD, ASI_RACPOV, ASI_MODODOB, ASI_PBODO, ASI_HITNO, " +
            "ASI_IZNOS, ASI_TIPGRESKE,ASI_STATUS  FROM ANALITIKA_PRESEKA p " +
            "join ANALITIKA_IZVODA i on p.PR_PIB=i.PR_PIB AND p.ANA_DSR_IZVOD=i.DSR_IZVOD " +
            "and  p.BAR_RACUN=i.BAR_RACUN and p.ASI_BROJSTAVKE=i.ASI_BROJSTAVKE  where PRE_BAR_RACUN=? " +
            "and convert(varchar,BNP_DATUM,110)=convert(varchar,?,110) and BNP_PRESEK=?";

    private Date date;
    private Form form;
    private Statement statement;
    private List<StatementItem> items;

    public ExportStatementAction(Form form) {
        super(TITLE);

        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // get date input from user
        boolean resultOk = getDateStatementNumber();

        if (resultOk) {
            try {
                int i = 1;
                String accountNumber = form.getSelectedRowValue("BAR_RACUN");
                java.sql.Date sqldate = new java.sql.Date(date.getTime());
                while (true) {
                    statement = new Statement();
                    items = new ArrayList<>();

                    // get header
                    PreparedStatement sqlStatement = DBConnection.getConnection().prepareStatement(HEADER_QUERY);

                    sqlStatement.setString(1, accountNumber);
                    sqlStatement.setDate(2, sqldate);
                    sqlStatement.setInt(3, i);

                    ResultSet rs = sqlStatement.executeQuery();
                    if (!rs.isBeforeFirst()) {
                        break;
                    }

                    while (rs.next()) {
                        mapHeaderRow(rs);
                    }

                    // get details
                    sqlStatement = DBConnection.getConnection().prepareStatement(ITEM_QUERY);

                    sqlStatement.setString(1, accountNumber);
                    sqlStatement.setDate(2, sqldate);
                    sqlStatement.setInt(3, i);

                    rs = sqlStatement.executeQuery();
                    while (rs.next()) {
                        mapItemsRow(rs);
                    }

                    // write to file
                    Statement.Items items = new Statement.Items();
                    items.setItem(this.items);
                    statement.setItems(items);
                    XmlHelper.writeToFile(statement, "statement_"
                            + statement.getOrderDate().getYear() + "-"
                            + statement.getOrderDate().getMonth()
                            + "-" + statement.getOrderDate().getDay()
                            + "_" + statement.getStatementNumber());
                    i++;
                }

                Toast.show(App.getMainFrame(), "Izvod uspešno exportovan.");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void mapHeaderRow(ResultSet resultSet) throws SQLException {
        GregorianCalendar cal;

        statement.setAccountNumber(resultSet.getString("BAR_RACUN"));
        cal = new GregorianCalendar();
        cal.setTime(resultSet.getDate("BNP_DATUM"));
        statement.setOrderDate(new XMLGregorianCalendarImpl(cal));

        statement.setStatementNumber(resultSet.getInt("BNP_PRESEK"));

        statement.setPreviousBalance(new BigDecimal(0));

        statement.setPayoutCount(resultSet.getInt("BNP_BRNATERET"));
        statement.setPayoutAmount(resultSet.getBigDecimal("BNP_UKTERET"));

        statement.setPaymentCount(resultSet.getInt("BNP_BRUKORIST"));
        statement.setPaymentAmount(resultSet.getBigDecimal("BNP_U_KORIST"));

        statement.setNewBalance(new BigDecimal(0));
    }

    private void mapItemsRow(ResultSet resultSet) throws SQLException {
        AccountDetails debtorAccountDetails;
        AccountDetails creditorAccountDetails;
        GregorianCalendar cal;
        StatementItem statementItem = new StatementItem();

        cal = new GregorianCalendar();
        cal.setTime(resultSet.getDate("BNP_DATUM"));

        statementItem.setDebtor(resultSet.getString("ASI_DUZNIK"));
        statementItem.setPaymentPurpose(resultSet.getString("ASI_SVRHA"));
        statementItem.setCreditor(resultSet.getString("ASI_POVERILAC"));

        cal = new GregorianCalendar();
        cal.setTime(resultSet.getDate("ASI_DATPRI"));
        statementItem.setOrderDate(new XMLGregorianCalendarImpl(cal));

        cal = new GregorianCalendar();
        cal.setTime(resultSet.getDate("ASI_DATVAL"));
        statementItem.setCurrencyDate(new XMLGregorianCalendarImpl(cal));

        debtorAccountDetails = new AccountDetails();
        debtorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACDUZ"));
        debtorAccountDetails.setModel(resultSet.getInt("ASI_MODZAD"));
        debtorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBZAD"));
        statementItem.setDebtorAccountDetails(debtorAccountDetails);

        creditorAccountDetails = new AccountDetails();
        creditorAccountDetails.setAccountNumber(resultSet.getString("ASI_RACPOV"));
        creditorAccountDetails.setModel(resultSet.getInt("ASI_MODODOB"));
        creditorAccountDetails.setReferenceNumber(resultSet.getString("ASI_PBODO"));
        statementItem.setCreditorAccountDetails(creditorAccountDetails);

        statementItem.setAmount(resultSet.getBigDecimal("ASI_IZNOS"));
        statementItem.setDirection("?");

        items.add(statementItem);

    }

    public boolean getDateStatementNumber() {
        boolean result = false;

        ValidationDatePicker datePicker = new ValidationDatePicker(0, BEFORE_INCLUDING);

        JPanel panel = new JPanel(new MigLayout());
        panel.add(new JLabel(INPUT_LABEL));
        panel.add(datePicker, "wrap");

        int dialogResult = JOptionPane.showConfirmDialog(null, panel,
                TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (dialogResult == JOptionPane.OK_OPTION) {
            this.date = datePicker.getDate();
            if (this.date != null) {
                result = true;
            }
        }

        return result;
    }
}