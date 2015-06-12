package factory;

import controller.TableController;
import model.TableModel;
import model.db.SchemaRow;

/**
 * Created by dejan on 6/9/2015.
 */
public interface TableControllerFactory {

    TableController create(TableModel model, SchemaRow schemaRow);

}
