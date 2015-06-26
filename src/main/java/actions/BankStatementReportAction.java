package actions;

import database.DBConnection;
import gui.standard.form.Form;
import gui.standard.form.components.ValidationDatePicker;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static gui.standard.form.components.ValidationDatePicker.E_VALID_DATES.BEFORE_INCLUDING;

/**
 * Created by Nikola on 14.6.2015..
 */
public class BankStatementReportAction extends AbstractAction {

    private static final String TITLE = "Izvod matorah";

    private Form form;

    public BankStatementReportAction(Form form) {
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
                JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/resources/jasper/izvod.jasper",
                        params, DBConnection.getConnection());

                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);

                JDialog dialog = new JDialog(form, "Izvod računa br. " + params.get("accountNumber"));
                dialog.setModal(true);
                dialog.setContentPane(jasperViewer.getContentPane());
                dialog.setSize(jasperViewer.getSize());
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Point newLocation = new Point(screenSize.width / 2 - (dialog.getWidth() / 2),
                        screenSize.height / 2 - (dialog.getHeight() / 2));
                dialog.setLocation(newLocation);
                dialog.setVisible(true);
            } catch (JRException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static final String INPUT_LABEL = "Unesite datum: ";

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
            params.put("date", dateFormat.format(datePicker.getDate()));
        }

        return ret;
    }
}