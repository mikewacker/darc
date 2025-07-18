package io.github.mikewacker.darc;

import static org.assertj.core.api.Assertions.assertThat;

import dagger.BindsInstance;
import dagger.Component;
import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public final class RequestContextTest {

    @RegisterExtension
    private static final DropwizardAppExtension<Configuration> app = new DropwizardAppExtension<>(TestApp.class);

    private static final OkHttpClient client = new OkHttpClient();

    @Test
    public void httpHeaders() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8080/http-headers")
                .header("Test-Header", "test")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.body().string()).isEqualTo("test");
        }
    }

    @Test
    public void uriInfo() throws IOException {
        Request request =
                new Request.Builder().url("http://localhost:8080/uri-info").build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.body().string()).isEqualTo("uri-info");
        }
    }

    @Test
    public void securityContext() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8080/security-context")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.body().string()).isEqualTo("false");
        }
    }

    @Test
    public void request() throws IOException {
        Request request =
                new Request.Builder().url("http://localhost:8080/request").build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.body().string()).isEqualTo("GET");
        }
    }

    @Test
    public void httpServletRequest() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8080/http-servlet-request")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.body().string()).isEqualTo("/http-servlet-request");
        }
    }

    @Test
    public void httpServletResponse() throws IOException {
        Request request = new Request.Builder()
                .url("http://localhost:8080/http-servlet-response")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.isSuccessful()).isTrue();
            assertThat(response.header("Test-Header")).isEqualTo("test");
        }
    }

    /** Test resource that uses {@link RequestContext}. */
    @Singleton
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    public static final class TestResource {

        private final RequestContext requestContext;

        @Inject
        public TestResource(RequestContext requestContext) {
            this.requestContext = requestContext;
        }

        @GET
        @Path("http-headers")
        public String httpHeaders() {
            return requestContext.httpHeaders().getHeaderString("Test-Header");
        }

        @GET
        @Path("uri-info")
        public String uriInfo() {
            return requestContext.uriInfo().getPath();
        }

        @GET
        @Path("security-context")
        public String securityContext() {
            return Boolean.toString(requestContext.securityContext().isSecure());
        }

        @GET
        @Path("request")
        public String request() {
            return requestContext.request().getMethod();
        }

        @GET
        @Path("http-servlet-request")
        public String httpServletRequest() {
            return requestContext.httpServletRequest().getRequestURI();
        }

        @GET
        @Path("http-servlet-response")
        public void httpServletResponse() {
            requestContext.httpServletResponse().addHeader("Test-Header", "test");
        }
    }

    /** Test application that runs {@link TestResource}. */
    public static final class TestApp extends Application<Configuration> {

        @Override
        public void run(Configuration config, Environment env) {
            TestResource resource = AppComponent.create(env).resource();
            env.jersey().register(resource);
        }

        /** Dagger component that provides {@link TestResource}. */
        @Component(modules = RequestContextModule.class)
        @Singleton
        interface AppComponent {

            static AppComponent create(Environment env) {
                return DaggerRequestContextTest_TestApp_AppComponent.factory().create(env);
            }

            TestResource resource();

            @Component.Factory
            interface Factory {

                AppComponent create(@BindsInstance Environment env);
            }
        }
    }
}
