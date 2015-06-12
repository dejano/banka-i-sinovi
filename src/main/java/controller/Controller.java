package controller;

import core.ConfigData;
import factory.DialogControllerFactory;
import model.GenericModel;
import model.config.MetaConfig;
import view.MainFrame;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class Controller extends BaseController {

    private final GenericModel model;
    private final ConfigData configData;
    private final MainFrame view;
    @Inject public DialogControllerFactory dialogControllerFactory;

    @Inject
    public Controller(GenericModel model, ConfigData configData) {
        this.model = model;
        this.configData = configData;

        Map<String, String> menuItems = new HashMap<>();
        for (MetaConfig metaConfig : configData.getMetaConfigMap().values()) {
            menuItems.put(metaConfig.getTableCode(), metaConfig.getTitle());
        }

        this.view = new MainFrame("Title", menuItems);
        this.view.addListener(this);
        this.model.addObserver(this.view);
    }

    @ActionCallback("createFormDialog")
    public void buttonOnClickCallback(String actionCommand) {
        DialogController dialogController = dialogControllerFactory.create(configData.getSchemaTableMap().get(actionCommand));
        dialogController.showGui();
    }

    @Override
    public void showGui() {
        this.view.showGui();
    }

}
