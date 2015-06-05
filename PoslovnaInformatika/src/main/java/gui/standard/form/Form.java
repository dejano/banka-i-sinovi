package gui.standard.form;

import gui.standard.Column;
import gui.standard.ColumnList;
import gui.standard.form.StatusBar.FormStatusEnum;

import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

public class Form extends JDialog {

	private static final long serialVersionUID = 1L;

	private JToolBar toolBar;

	private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp,
			btnNext, btnNextForm, btnPickup, btnRefresh, btnRollback,
			btnSearch, btnPrevious;

	private JTable dataTable = new JTable();
	private TableModel tableModel;

	private DataPanel dataPanel;
	private StatusBar statusBar;

	private ColumnList zoomList;

	public Form(FormMetaData fmd) throws SQLException {
		setLocationRelativeTo(null);
		setLayout(new MigLayout("fill"));
		setSize(new Dimension(800, 600));
		setModal(true);
		setTitle(fmd.getTitle());

		initTable(fmd);
		initGui();
		initToolbar();
		initStatusBar();
	}

	private void initToolbar() {
		toolBar = new JToolBar();
		btnSearch = new JButton(new SearchAction(this));
		toolBar.add(btnSearch);

		btnRefresh = new JButton(new RefreshAction(this));
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
		statusBar = new StatusBar();

		add(statusBar, "dock south");
	}

	private void initGui() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("fillx"));

		dataPanel = new DataPanel();
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

	private void initTable(FormMetaData fmd) throws SQLException {
		JScrollPane scrollPane = new JScrollPane(dataTable);
		add(scrollPane, "grow, wrap");

		tableModel = new TableModel(MosquitoSingletone.getInstance()
				.getMetaTable(fmd.getTableName()));

		dataTable.setModel(tableModel);

		tableModel.open();

		dataTable.setRowSelectionAllowed(true);
		dataTable.setColumnSelectionAllowed(false);
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		dataTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting())
							return;
						
						Form.this.setMode(FormStatusEnum.EDIT);
						sync();
					}
				});
	}

	private void sync() {
		int index = dataTable.getSelectedRow();
		if (index < 0) {
			// tfSifra.setText("");
			// tfNaziv.setText("");
			return;
		}
		// String sifra = (String) tableModel.getValueAt(index, 0);
		// String naziv = (String) tableModel.getValueAt(index, 1);
		// tfSifra.setText(sifra);
		// tfNaziv.setText(naziv);
	}

	public void removeRow() {
		int index = dataTable.getSelectedRow();
		if (index == -1)
			return;

		int newIndex = index;

		if (index == tableModel.getRowCount() - 1)
			newIndex--;

		try {
			tableModel.deleteRow(index);

			if (tableModel.getRowCount() > 0)
				dataTable.setRowSelectionInterval(newIndex, newIndex);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void createZoomList() {
		int selectedRowIndex = dataTable.getSelectedRow();
		int columnCount = tableModel.getColumnCount();

		zoomList = new ColumnList();

		for (int i = 0; i < columnCount; i++) {
			Column column = new Column(tableModel.getColumnName(i),
					tableModel.getValueAt(selectedRowIndex, i));
			zoomList.add(column);
		}
	}

	public void createNextList() {
		int selectedRowIndex = dataTable.getSelectedRow();

		zoomList = new ColumnList();

		zoomList.add(new Column("drzava.dr_sifra", tableModel.getValueAt(
				selectedRowIndex, 0)));
		zoomList.add(new Column("drzava.dr_naziv", tableModel.getValueAt(
				selectedRowIndex, 1)));
	}

	public JTable getDataTable() {
		return dataTable;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	public FormStatusEnum getMode(){
		return this.statusBar.getStatus();
	}
	
	public void setMode(FormStatusEnum mode) {
		this.statusBar.setStatus(mode);
	}

	public void enablePickup() {
		btnPickup.setEnabled(true);
	}

	public void disablePickup() {
		btnPickup.setEnabled(false);
	}

	public ColumnList getColumnList() {
		return zoomList;
	}

	public DataPanel getDataPanel() {
		return dataPanel;
	}

	public void setDataPanel(DataPanel dataPanel) {
		this.dataPanel = dataPanel;
	}
}
