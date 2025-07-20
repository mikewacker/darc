package io.github.mikewacker.darc;

import io.dropwizard.core.setup.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

/** Implementation of {@link RequestContext} for Dropwizard. */
@Singleton
final class DropwizardRequestContext implements RequestContext {

    private Provider<HttpHeaders> httpHeadersProvider;
    private Provider<UriInfo> uriInfoProvider;
    private Provider<SecurityContext> securityContextProvider;
    private Provider<Request> requestProvider;
    private Provider<HttpServletRequest> httpServletRequestProvider;
    private Provider<HttpServletResponse> httpServletResponseProvider;

    @Inject
    public DropwizardRequestContext(Environment env) {
        env.jersey().register(new RequestContextInjector());
    }

    @Override
    public HttpHeaders httpHeaders() {
        return httpHeadersProvider.get();
    }

    @Override
    public UriInfo uriInfo() {
        return uriInfoProvider.get();
    }

    @Override
    public SecurityContext securityContext() {
        return securityContextProvider.get();
    }

    @Override
    public Request request() {
        return requestProvider.get();
    }

    @Override
    public HttpServletRequest httpServletRequest() {
        return httpServletRequestProvider.get();
    }

    @Override
    public HttpServletResponse httpServletResponse() {
        return httpServletResponseProvider.get();
    }

    /** Injects request context using Jersey/HK2. */
    private final class RequestContextInjector implements ContainerLifecycleListener {

        @Override
        public void onStartup(Container container) {
            InjectionManager injectionManager =
                    container.getApplicationHandler().getInjectionManager();
            httpHeadersProvider = injectProvider(injectionManager, HttpHeaders.class);
            uriInfoProvider = injectProvider(injectionManager, UriInfo.class);
            securityContextProvider = injectProvider(injectionManager, SecurityContext.class);
            requestProvider = injectProvider(injectionManager, Request.class);
            httpServletRequestProvider = injectProvider(injectionManager, HttpServletRequest.class);
            httpServletResponseProvider = injectProvider(injectionManager, HttpServletResponse.class);
        }

        @Override
        public void onReload(Container container) {}

        @Override
        public void onShutdown(Container container) {}

        /** Injects a provider for the type. */
        private <T> Provider<T> injectProvider(InjectionManager injectionManager, Class<T> type) {
            return injectionManager.getInstance(new ProviderType(type));
        }
    }

    /** Parameterized type for a provider. */
    private static final class ProviderType implements ParameterizedType {

        private final Class<?> type;

        public ProviderType(Class<?> type) {
            this.type = type;
        }

        @Override
        public Type getRawType() {
            return Provider.class;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
