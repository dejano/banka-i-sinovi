package controller;

import com.google.inject.assistedinject.Assisted;
import core.ConfigData;
import db.DatabaseStorage;
import factory.DialogControllerFactory;
import factory.TableControllerFactory;
import model.TableModel;
import model.config.ZoomConfig;
import model.db.SchemaColumn;
import model.db.SchemaRow;
import view.FormDialog;
import view.ToolBar;

import javax.inject.Inject;
import javax.swing.*;
import java.util.*;

public class DialogController extends BaseController implements Observer {

    private final SchemaRow model;
    private final FormDialog view;
    private final ToolBar toolBar;
    private final DatabaseStorage storage;
    @Inject
    public DialogControllerFactory dialogControllerFactory;
    @Inject
    private TableControllerFactory tableControllerFactory;
    private ConfigData configData;
    private TableController tableController;
    private Map<String, String> zoomData;
    private DialogController zoomParentController;
    private TableModel tableModel;
    private PanelController panelController;

    @Inject
    public DialogController(@Assisted SchemaRow model, ConfigData configData, DatabaseStorage storage) {
        this.configData = configData;
        this.storage = storage;
        this.model = model;
        this.view = new FormDialog(null, model.getTableName());
        this.view.addListener(this);
        this.model.addObserver(this.view);
        this.toolBar = new ToolBar(this);
        toolBar.addListener(this);
        this.view.setToolBar(toolBar);
    }

    private void createComponents() {
        tableModel = new TableModel(model.getColumnNames(), model.getColumnCodes(), 0);
        tableController = tableControllerFactory.create(tableModel, model);
        this.tableController.addObserver(this);
        this.view.setTable(tableController.getView());

        panelController = new PanelController(model, tableModel);
        this.view.setPanel(panelController.getView());
        panelController.getView().addListener(this);
        tableController.addObserver(panelController);
    }

    @Override
    public void showGui() {
        createComponents();
        this.view.showGui();
    }

    @ActionCallback("createFormDialog")
    public void buttonOnClickCallback(String actionCommand) {
        DialogController dialogController = dialogControllerFactory.create(configData.getSchemaTableMap().get(actionCommand));

        Vector<String> zoomColumns = model.getLookupColumnCodes(model.getTableCode(), actionCommand);
        Map<String, String> zoomData = new HashMap<>();
        for (String zoomColumn : zoomColumns) {
            zoomData.put(zoomColumn, null);
        }

//        for (ZoomConfig zoomConfig : model.getZoomList()) {
//            if (zoomConfig.getTable().equals(actionCommand)) {
//                for (String zoomColumn : zoomConfig.getColumns()) {
//                    zoomData.put(zoomColumn, null);
//                }
//            }
//        }

        dialogController.setZoomCallback(zoomData, this);
        dialogController.showGui();
    }

    @ActionCallback("refresh")
    public void refreshAction(String actionCommand) {
        getTableController().refresh();
    }

    @ActionCallback("remove")
    public void removeAction(String actionCommand) {
        try {
//            Map<String, String> rowValues = new HashMap<>();
//            for (SchemaColumn column : model.getPrimaryKeys()) {
//                rowValues.put(column.getCode(), tableController.getColumnValueByCode(tableController.getSelectedRowIndex(), column.getCode()));
//            }
//            storage.delete(model, rowValues, tableController);

            // Update
            Map<String, String> rowValues = new HashMap<>();
            for (SchemaColumn column : model.getSchemaColumns()) {
                rowValues.put(column.getCode(), tableController.getColumnValueByCode(tableController.getSelectedRowIndex(), column.getCode()));
            }
            storage.update(model, rowValues, tableController);
        } catch (Throwable e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.view, e.getMessage());
        } finally {
            tableController.refresh();
        }
    }

    @ActionCallback("next")
    public void nextAction(String actionCommand) {
        getTableController().refresh();
    }

    @ActionCallback("pickup")
    public void pickupAction(String actionCommand) {
        int index = getTableController().getSelectedRowIndex();
        Map<String, String> results = new HashMap<>();
        if (index >= 0) {
            for (String columnCode : zoomData.keySet()) {
                String columnWithoutPrefix = removeDotPrefix(columnCode);
                String columnValue = getTableModel().getColumnValueByCode(index, columnWithoutPrefix);
                results.put(columnCode, columnValue);
            }
        }
        zoomParentController.setZoomData(results);
        toolBar.getBtnPickup().setEnabled(false);
        view.setVisible(false);
        view.dispose();
        zoomParentController.onZoomDialogClosed();
    }

    private void onZoomDialogClosed() {
        panelController.updateWithZoomData(zoomData);
    }

    private void setZoomCallback(Map<String, String> zoomData, DialogController dialogController) {
        this.zoomData = zoomData;
        zoomParentController = dialogController;
        toolBar.getBtnPickup().setEnabled(true);
    }

    /**
     * Receive string in following format Something.hello and returns hello.
     */
    public static String removeDotPrefix(String stringWithPrefix) {
        String[] splited = stringWithPrefix.split("\\.");
        if (splited.length > 1) {
            return splited[1];
        }
        return stringWithPrefix;
    }

    public TableController getTableController() {
        return tableController;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setZoomData(Map<String, String> zoomData) {
        this.zoomData = zoomData;
    }

    @Override
    public void update(Observable o, Object arg) {
        int index = (int) arg;
        if (index < 0) {
            toolBar.getBtnDelete().setEnabled(false);
            toolBar.getBtnNext().setEnabled(false);
        } else if (index >= 0) {
            toolBar.getBtnDelete().setEnabled(true);
            toolBar.getBtnNext().setEnabled(true);
        }
    }
}
