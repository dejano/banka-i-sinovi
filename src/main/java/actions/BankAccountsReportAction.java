package actions;

import app.App;
import app.AppData;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import database.DBConnection;
import gui.dialog.Toast;
import gui.standard.ColumnValue;
import gui.standard.form.misc.ProcedureCallFactory;
import gui.standard.form.misc.StatementExecutor;
import messages.QuestionMessages;
import meta.MosquitoSingletone;
import meta.SuperMetaTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import xml.XmlHelper;
import xml.cmn.AccountDetails;
import xml.cmn.BankDetails;
import xml.mp.Mt102;
import xml.mp.Mt102Payment;
import xml.mp.ObjectFactory;
import xml.po.PaymentOrder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Nikola on 14.6.2015..
 */
public class BankAccountsReportAction extends AbstractAction {

    private static final String TITLE = "Stanje računa klijenata";

    public BankAccountsReportAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, String> params = new HashMap(1);
        params.put("STATUS", AppData.getInstance().getValue(AppData.AppDataEnum.PIB_BANKE));

        try {
            JasperPrint jp = JasperFillManager.fillReport(
                    getClass().getResource("/jasper/Zaposleni.jasper").openStream(),
                    params, DBConnection.getConnection());

            JasperViewer.viewReport(jp, false);
        } catch (JRException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}