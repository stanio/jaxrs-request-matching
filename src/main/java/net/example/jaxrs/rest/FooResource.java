package net.example.jaxrs.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("rest")
public interface FooResource {

    @GET
    String npoba();

    @GET
    @Path("foo")
    String foo();

}
