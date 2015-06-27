package actions;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import database.DBConnection;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static gui.standard.form.components.ValidationDatePicker.E_VALID_DATES.BEFORE_INCLUDING;

public class ExportStatementAction extends AbstractAction {

    private static final String TITLE = "Eksport izvoda";
    private static final String INPUT_LABEL = "Unesite datum: ";
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
        Map<String, Object> params = new HashMap(3);

        params.put("accountNumber", form.getSelectedRowValue("BAR_RACUN"));

        boolean resultOk = getDateStatementNumber(params);

        if (resultOk) {
            try {
                statement = new Statement();
                items = new ArrayList<>();
                PreparedStatement sqlStatement =
                        DBConnection.getConnection().prepareStatement(header);
                java.sql.Date sqldate = new java.sql.Date(date.getTime());
                sqlStatement.setString(1,  form.getSelectedRowValue("BAR_RACUN"));
                sqlStatement.setDate(2, sqldate);
                sqlStatement.setInt(3, 3);

                ResultSet rs = sqlStatement.executeQuery();

                while (rs.next()) {
                    mapHeaderRow(rs);
                }

                sqlStatement =
                        DBConnection.getConnection().prepareStatement(item);
                sqlStatement.setString(1,  form.getSelectedRowValue("BAR_RACUN"));
                sqlStatement.setDate(2, sqldate);
                sqlStatement.setInt(3, 3);

                rs = sqlStatement.executeQuery();

                while (rs.next()) {
                    mapItemsRow(rs);
                }
                Statement.Items items = new Statement.Items();
                items.setItem(this.items);
                statement.setItems(items);
                XmlHelper.writeToFile(statement, "statement_" + System.nanoTime());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

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

    private void mapHeaderRow(ResultSet resultSet) throws SQLException {
        GregorianCalendar cal;

        statement.setAccountNumber(resultSet.getString("BAR_RACUN"));

        cal = new GregorianCalendar();
        cal.setTime(resultSet.getDate("BNP_DATUM"));
        statement.setOrderDate(new XMLGregorianCalendarImpl(cal));

        statement.setStatementNumber(resultSet.getInt("DSR_IZVOD"));

        statement.setPreviousBalance(new BigDecimal(0));

        statement.setPayoutCount(resultSet.getInt("BNP_BRNATERET"));
        statement.setPayoutAmount(resultSet.getBigDecimal("BNP_UKTERET"));

        statement.setPaymentCount(resultSet.getInt("BNP_BRUKORIST"));
        statement.setPaymentAmount(resultSet.getBigDecimal("BNP_U_KORIST"));

        statement.setNewBalance(new BigDecimal(0));
    }

    public boolean getDateStatementNumber(Map<String, Object> params) {
        boolean ret = false;

        ValidationDatePicker datePicker = new ValidationDatePicker(0, BEFORE_INCLUDING);

        JPanel myPanel = new JPanel(new MigLayout());
        myPanel.add(new JLabel(INPUT_LABEL));
        myPanel.add(datePicker, "wrap");

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            ret = true;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            this.date = datePicker.getDate();
            params.put("date", dateFormat.format(datePicker.getDate()));
        }

        return ret;
    }

    private final String header = "SELECT *  FROM PI.dbo.PRENOS_IZVODA___PRESEK  WHERE BAR_RACUN = ?  AND BNP_DATUM = ?  AND BNP_PRESEK = ?;";
    private final String item = "SELECT PRE_PR_PIB\n" +
            "      ,PRE_BAR_RACUN\n" +
            "      ,p.DSR_IZVOD\n" +
            "      ,BNP_DATUM\n" +
            "      ,BNP_PRESEK\n" +
            "      ,APR_RBR\n" +
            "      ,p.PR_PIB\n" +
            "      ,p.BAR_RACUN\n" +
            "      ,ANA_DSR_IZVOD\n" +
            "      ,p.ASI_BROJSTAVKE\n" +
            "\t  ,VA_IFRA\n" +
            "      ,NM_SIFRA\n" +
            "      ,VPL_OZNAKA\n" +
            "      ,ID_NALOGA_PL\n" +
            "      ,ASI_DUZNIK\n" +
            "      ,ASI_SVRHA\n" +
            "      ,ASI_POVERILAC\n" +
            "      ,ASI_DATPRI\n" +
            "      ,ASI_DATVAL\n" +
            "      ,ASI_RACDUZ\n" +
            "      ,ASI_MODZAD\n" +
            "      ,ASI_PBZAD\n" +
            "      ,ASI_RACPOV\n" +
            "      ,ASI_MODODOB\n" +
            "      ,ASI_PBODO\n" +
            "      ,ASI_HITNO\n" +
            "      ,ASI_IZNOS\n" +
            "      ,ASI_TIPGRESKE\n" +
            "      ,ASI_STATUS\n" +
            "  FROM PI.dbo.ANALITIKA_PRESEKA p join ANALITIKA_IZVODA i on p.PR_PIB=i.PR_PIB AND p.ANA_DSR_IZVOD=i.DSR_IZVOD and\n" +
            "  p.BAR_RACUN=i.BAR_RACUN and p.ASI_BROJSTAVKE=i.ASI_BROJSTAVKE\n" +
            "  where PRE_BAR_RACUN=? and BNP_DATUM=? and BNP_PRESEK=?";
}