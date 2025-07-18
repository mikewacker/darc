package io.github.mikewacker.darc.example;

import io.github.mikewacker.darc.RequestContext;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/** Example resource that uses {@link RequestContext}. */
@Singleton
@Path("example")
@Produces(MediaType.TEXT_PLAIN)
public final class ExampleResource {

    private final RequestContext requestContext;

    @Inject
    public ExampleResource(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /** Gets the URI path. */
    @GET
    public String example() {
        return requestContext.uriInfo().getPath();
    }
}
