package factory;

import controller.DialogController;
import model.db.SchemaRow;

/**
 * Created by dejan on 6/9/2015.
 */
public interface DialogControllerFactory {

    DialogController create(SchemaRow model);
}
