package core;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import controller.DialogController;
import db.TransactionInterceptor;
import db.Transactional;
import factory.DialogControllerFactory;
import factory.TableControllerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
//        bind(MosquitoFacade.class).to(MosquitoFacadeImpl.class);
//        bind(Storage.class).to(DatabaseStorage.class);
//        bind(TableControllerFactory.class).toProvider(
//                FactoryProvider.newFactory(TableControllerFactory.class, DialogController.class));

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        requestInjection(transactionInterceptor);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionInterceptor);
        install(new FactoryModuleBuilder()
                .implement(DialogController.class, DialogController.class)
                .build(TableControllerFactory.class));
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/DBConnection.properties"));
            Names.bindProperties(binder(), props);

            install(new FactoryModuleBuilder()
                    .implement(DialogController.class, DialogController.class)
                    .build(DialogControllerFactory.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
