package factory;

import db.DatabaseStorage;
import model.TableModel;

public interface DatabaseStorageFactory {

    DatabaseStorage create(TableModel model);
}
