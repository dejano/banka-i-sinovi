package controller.action;

import controller.ActionCallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dejan on 6/10/2015.
 */
public class ToolBarAction extends AbstractAction {


    private List<Object> listeners;

    public ToolBarAction(Icon icon, String name, String actionCommand, List<Object> listeners) {
        this(name, actionCommand, listeners);
        putValue(SMALL_ICON, icon);
    }

    public ToolBarAction(String name, String actionCommand, List<Object> listeners) {
        putValue(NAME, name);
        putValue(ACTION_COMMAND_KEY, actionCommand);
        this.listeners = listeners;
    }

    public void actionPerformed(ActionEvent e) {
        for (Object o : listeners) {
            for (Method m : o.getClass().getMethods()) {
                for (Annotation a : m.getAnnotations()) {
                    if (a instanceof ActionCallback) {
                        ActionCallback callback = (ActionCallback) a;
                        if (callback.value().equals(e.getActionCommand())) {
                            try {
                                m.invoke(o, e.getActionCommand());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
                                System.out.println(e1.getMessage());
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
