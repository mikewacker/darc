# DARC: Dagger and Request Context (for Dropwizard)

If I'm building a Dropwizard application, how do I inject `HttpHeaders` using Dagger?

1. Inject `ResourceContext` into your class.
2. Call the `httpHeaders()` method of the `ResourceContext` object.
3. Add `RequestContextModule` to your Dagger component.
    - This Dagger module only depends on the Dropwizard `Environment`.

If you want a code example, see [`ExampleResource`](/example/src/main/java/io/github/mikewacker/darc/example/ExampleResource.java) and [`ExampleApp`](/example/src/main/java/io/github/mikewacker/darc/example/ExampleApp.java).

---

`ResourceContext` contains:

- `HttpHeaders`
- `UriInfo`
- `SecurityContext`
- `Request`
- `HttpServletRequest`
- `HttpServletResponse`

**Limitation**

This context is thread-local; it is only accessible from the thread that handles the request.
(The same limitation would apply if you used HK2 to inject `Provider<HttpHeaders>` into a class.)
