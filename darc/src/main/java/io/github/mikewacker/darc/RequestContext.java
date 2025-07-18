package io.github.mikewacker.darc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

/**
 * Provider for request-scoped context.
 * <p>
 * This context is thread-local; it is only accessible from the thread that handles the request.
 */
public interface RequestContext {

    /**
     * Gets the HTTP headers.
     *
     * @return HTTP headers
     */
    HttpHeaders httpHeaders();

    /**
     * Gets the URI information.
     *
     * @return URI information
     */
    UriInfo uriInfo();

    /**
     * Gets the security context.
     *
     * @return security context
     */
    SecurityContext securityContext();

    /**
     * Gets the request.
     *
     * @return request
     */
    Request request();

    /**
     * Gets the HTTP servlet request.
     *
     * @return HTTP servlet request
     */
    HttpServletRequest httpServletRequest();

    /**
     * Gets the HTTP servlet response.
     *
     * @return HTTP servlet response
     */
    HttpServletResponse httpServletResponse();
}
