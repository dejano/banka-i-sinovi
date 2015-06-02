package gui.standard.form;

import gui.standard.Column;
import gui.standard.ColumnList;

import java.awt.Dimension;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import meta.FormMetaData;
import meta.MosquitoSingletone;
import net.miginfocom.swing.MigLayout;
import actions.standard.AddAction;
import actions.standard.CommitAction;
import actions.standard.DeleteAction;
import actions.standard.FirstAction;
import actions.standard.HelpAction;
import actions.standard.LastAction;
import actions.standard.NextAction;
import actions.standard.NextFormAction;
import actions.standard.PickupAction;
import actions.standard.PreviousAction;
import actions.standard.RefreshAction;
import actions.standard.RollbackAction;
import actions.standard.SearchAction;

public class StandardForm extends JDialog {

	private static final long serialVersionUID = 1L;

	public enum FormStatusEnum {
		EDIT, ADD, SEARCH
	}

	private JToolBar toolBar;
	private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp,
			btnNext, btnNextForm, btnPickup, btnRefresh, btnRollback,
			btnSearch, btnPrevious;

	private JTable tblGrid = new JTable();
	private StandardTableModel tableModel;

	private Map<String, JComponent> inputs = new HashMap<String, JComponent>();

	private FormStatusEnum mode;
	private JPanel statusBar;
	private JLabel lblStatus;

	private ColumnList zoomList;

	public StandardForm(FormMetaData fmd) throws SQLException {
		setLayout(new MigLayout("fill"));
		setSize(new Dimension(800, 600));
		// setLocationRelativeTo(MainFrame.getInstance());
		setModal(true);
		setTitle(fmd.getTitle());

		tableModel = new StandardTableModel(MosquitoSingletone.getInstance()
				.getMetaTable(fmd.getTableName()));

		initToolbar();
		initStatusBar();
		initTable();
		initGui();
	}

	public FormStatusEnum getMode() {
		return mode;
	}

	public void setMode(FormStatusEnum mode) {
		this.mode = mode;
	}

	private void initTable() throws SQLException {
		JScrollPane scrollPane = new JScrollPane(tblGrid);
		add(scrollPane, "grow, wrap");

		tblGrid.setModel(tableModel);

		tableModel.open();

		tblGrid.setRowSelectionAllowed(true);
		tblGrid.setColumnSelectionAllowed(false);
		tblGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tblGrid.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;
						sync();
					}
				});
	}

	public StandardTableModel getTableModel() {
		return tableModel;
	}

	public JTable getTblGrid() {
		return tblGrid;
	}

	private void sync() {
		int index = tblGrid.getSelectedRow();
		if (index < 0) {
			// tfSifra.setText("");
			// tfNaziv.setText("");
			return;
		}
		String sifra = (String) tableModel.getValueAt(index, 0);
		String naziv = (String) tableModel.getValueAt(index, 1);
		// tfSifra.setText(sifra);
		// tfNaziv.setText(naziv);
	}

	private void removeRow() {
		int index = tblGrid.getSelectedRow();
		if (index == -1) // Ako nema selektovanog reda (tabela prazna)
			return; // izlazak
		// kada obrisemo tekuci red, selektovacemo sledeci (newindex):
		int newIndex = index;
		// sem ako se obrise poslednji red, tada selektujemo prethodni
		if (index == tableModel.getRowCount() - 1)
			newIndex--;
		try {
			StandardTableModel dtm = (StandardTableModel) tblGrid.getModel();
			dtm.deleteRow(index);
			if (tableModel.getRowCount() > 0)
				tblGrid.setRowSelectionInterval(newIndex, newIndex);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initToolbar() {

		toolBar = new JToolBar();
		btnSearch = new JButton(new SearchAction(this));
		toolBar.add(btnSearch);

		btnRefresh = new JButton(new RefreshAction());
		toolBar.add(btnRefresh);

		btnPickup = new JButton(new PickupAction(this));
		toolBar.add(btnPickup);
		btnPickup.setEnabled(false);

		btnHelp = new JButton(new HelpAction());
		toolBar.add(btnHelp);

		toolBar.addSeparator();

		btnFirst = new JButton(new FirstAction(this));
		toolBar.add(btnFirst);

		btnPrevious = new JButton(new PreviousAction(this));
		toolBar.add(btnPrevious);

		btnNext = new JButton(new NextAction(this));
		toolBar.add(btnNext);

		btnLast = new JButton(new LastAction(this));
		toolBar.add(btnLast);

		toolBar.addSeparator();

		btnAdd = new JButton(new AddAction(this));
		toolBar.add(btnAdd);

		btnDelete = new JButton(new DeleteAction(this));
		toolBar.add(btnDelete);

		toolBar.addSeparator();

		btnNextForm = new JButton(new NextFormAction(this));
		toolBar.add(btnNextForm);

		add(toolBar, "dock north");
	}

	private void initStatusBar() {
		statusBar = new JPanel();

		lblStatus = new JLabel(String.valueOf(getMode()));
		statusBar.add(lblStatus);
		add(statusBar, "dock south");
	}

	private void initGui() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("fillx"));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new MigLayout("gapx 15px"));

		JPanel buttonsPanel = new JPanel();
		btnCommit = new JButton(new CommitAction(this));
		btnRollback = new JButton(new RollbackAction(this));

		// JLabel lblSifra = new JLabel("Šifra države:");
		// JLabel lblNaziv = new JLabel("Naziv države:");
		//
		// dataPanel.add(lblSifra);
		// // dataPanel.add(tfSifra, "wrap");
		// dataPanel.add(lblNaziv);
		// // dataPanel.add(tfNaziv);
		// bottomPanel.add(dataPanel);

		buttonsPanel.setLayout(new MigLayout("wrap"));
		buttonsPanel.add(btnCommit);
		buttonsPanel.add(btnRollback);
		bottomPanel.add(buttonsPanel, "dock east");

		add(bottomPanel, "grow, wrap");
	}

	public void enablePickup() {
		btnPickup.setEnabled(true);
	}

	public void disablePickup() {
		btnPickup.setEnabled(false);
	}

	public JLabel getLblStatus() {
		return lblStatus;
	}

	public void setLblStatus(JLabel lblStatus) {
		this.lblStatus = lblStatus;
	}

	public ColumnList getColumnList() {
		return zoomList;
	}

	public void createZoomList() {
		int selectedRowIndex = tblGrid.getSelectedRow();
		int columnCount = tableModel.getColumnCount();

		zoomList = new ColumnList();

		for (int i = 0; i < columnCount; i++) {
			Column column = new Column(tableModel.getColumnName(i),
					tableModel.getValueAt(selectedRowIndex, i));
			zoomList.add(column);
		}
	}

	public void createNextList() {
		int selectedRowIndex = tblGrid.getSelectedRow();

		zoomList = new ColumnList();

		zoomList.add(new Column("drzava.dr_sifra", tableModel.getValueAt(
				selectedRowIndex, 0)));
		zoomList.add(new Column("drzava.dr_naziv", tableModel.getValueAt(
				selectedRowIndex, 1)));
	}

}
