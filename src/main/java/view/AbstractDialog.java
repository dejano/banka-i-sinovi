package view;

import model.db.SchemaRow;

/**
 * Created by dejan on 6/9/2015.
 */
public interface AbstractDialog extends AbstractView {

    void setToolBar(ToolBar toolBar);
    void setTable(Table table);
    void setPanel(DetailsPanel detailsPanel);
}
