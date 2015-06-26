package actions;

import app.App;
import gui.dialog.Toast;
import gui.standard.form.components.ValidationDatePicker;
import meta.MosquitoSingletone;
import meta.SuperMetaTable;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static gui.standard.form.components.ValidationDatePicker.E_VALID_DATES.BEFORE_INCLUDING;

public class ExportStatementAction extends AbstractAction {

    private static final String TITLE = "Eksport izvoda";

    private int i = 1;

    private SuperMetaTable orderMetaTable;
    private SuperMetaTable orderItemMetaTable;
    private SuperMetaTable paymentOrderMetaTable;
    private SuperMetaTable bankDetailsMetaTable;

    public ExportStatementAction() {
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

        JDialog jDialog = new JDialog();
        jDialog.setSize(300, 200);
//        jDialog.setLayout(new MigLayout("fill"));
        jDialog.setModal(true);
        jDialog.setTitle(TITLE);
        jDialog.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point newLocation;

        newLocation = new Point(screenSize.width / 2 - (jDialog.getWidth() / 2),
                screenSize.height / 2 - (jDialog.getHeight() / 2));

        jDialog.setLocation(newLocation);


        ValidationDatePicker datePicker = new ValidationDatePicker(0, BEFORE_INCLUDING);
        JTextField statementNumberInput = new JTextField(20);

        JPanel myPanel = new JPanel(new MigLayout());
        myPanel.add(new JLabel("Datum"));
        myPanel.add(datePicker, "wrap");
        myPanel.add(new JLabel("Racun"));
        myPanel.add(statementNumberInput, "wrap");
        JButton okButton = new JButton("Eksport");
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toast.show(App.getMainFrame(), "Pokrenut eksport.");
            }
        });

        myPanel.add(okButton, "dock south");
        jDialog.add(myPanel);

        jDialog.setVisible(true);

//        Toast.show(App.getMainFrame(), "Pokrenut eksport.");
//
//        new Thread(new Runnable() {
//            public void run() {
//
//                Toast.show(App.getMainFrame(), "Izvrsen eksport.");
//            }
//        }).start();
    }


}