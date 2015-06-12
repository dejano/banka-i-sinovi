package core;

import controller.Controller;
import model.GenericModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Application {

    private Controller controller;
    private GenericModel genericModel;
    private ConfigData configData;


    @Inject
    public Application(Controller controller, GenericModel genericModel, ConfigData configData) {
        this.controller = controller;
        this.genericModel = genericModel;
        this.configData = configData;
    }

    public void run() {
        controller.showGui();
    }
}
