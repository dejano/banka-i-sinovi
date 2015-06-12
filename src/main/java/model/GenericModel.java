package model;

/**
 * Created by dejan on 6/9/2015.
 */
public class GenericModel extends Model {

    public GenericModel() {
    }

    public void inc() {
        notifyChanged(1);
    }
}
