package io.github.mikewacker.darc;

import dagger.Binds;
import dagger.Module;
import io.dropwizard.core.setup.Environment;

/**
 * Dagger module that binds {@link RequestContext}.
 * <p>
 * Depends on an unbound {@link Environment}.
 */
@Module
public abstract class RequestContextModule {

    @Binds
    abstract RequestContext bindRequestContext(DropwizardRequestContext impl);

    private RequestContextModule() {}
}
