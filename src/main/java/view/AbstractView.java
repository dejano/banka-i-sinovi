package view;

import model.db.SchemaRow;

import java.util.Observer;

/**
 * Created by dejan on 6/9/2015.
 */
public interface AbstractView extends Observer {

    void createGui();

    void showGui();
    void addListener(Object o);
}
