package core;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApplicationModule());

        // load config files
        ConfigData configData = injector.getInstance(ConfigData.class);
        configData.readConfigurations();

        // run application
        Application application = injector.getInstance(Application.class);
        application.run();
    }
}
