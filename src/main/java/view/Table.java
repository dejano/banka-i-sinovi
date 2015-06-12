package view;

import model.TableModel;

import javax.swing.*;

/**
 * Created by dejan on 6/9/2015.
 */
public class Table extends JTable {

    public Table(TableModel model) {
        super(model);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
