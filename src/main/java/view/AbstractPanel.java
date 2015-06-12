package view;

import model.db.SchemaRow;

/**
 * Created by dejan on 6/10/2015.
 */
public interface AbstractPanel extends AbstractView {
    public void createGui(SchemaRow schemaRow);
}
