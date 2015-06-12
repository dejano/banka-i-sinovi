package model;

import java.beans.PropertyChangeListener;
import java.util.Observable;

/**
 *
 * Contains generic methods which all models need to implement.
 */
public abstract class Model extends Observable {

    public void notifyChanged(Object arg) {
        System.out.println("Model.notifyChanged");
        setChanged();
        notifyObservers(arg);
    }
}
