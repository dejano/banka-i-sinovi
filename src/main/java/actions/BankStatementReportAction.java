package actions;

import app.AppData;
import database.DBConnection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikola on 14.6.2015..
 */
public class BankStatementReportAction extends AbstractAction {

    private static final String TITLE = "Izvod matorah";

    public BankStatementReportAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, Object> params = new HashMap(1);

        try {
            JasperPrint jp = JasperFillManager.fillReport("src/main/resources/jasper/izvod.jasper",
                    params, DBConnection.getConnection());

            JasperViewer.viewReport(jp, false);
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }
}