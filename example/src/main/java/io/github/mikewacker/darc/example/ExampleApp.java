package io.github.mikewacker.darc.example;

import dagger.BindsInstance;
import dagger.Component;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.github.mikewacker.darc.RequestContextModule;
import jakarta.inject.Singleton;

/** Application that runs {@link ExampleResource}. */
public final class ExampleApp extends Application<Configuration> {

    @Override
    public void run(Configuration config, Environment env) {
        ExampleResource resource = AppComponent.create(env).resource();
        env.jersey().register(resource);
    }

    /** Dagger component that provides {@link ExampleResource}. */
    @Component(modules = RequestContextModule.class)
    @Singleton
    interface AppComponent {

        static AppComponent create(Environment env) {
            return DaggerExampleApp_AppComponent.factory().create(env);
        }

        ExampleResource resource();

        @Component.Factory
        interface Factory {

            AppComponent create(@BindsInstance Environment env);
        }
    }
}
