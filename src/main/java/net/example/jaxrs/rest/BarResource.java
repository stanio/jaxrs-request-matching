package net.example.jaxrs.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("rest")
public interface BarResource {

    @GET
    @Path("foo/bar")
    String bar();

    @GET
    @Path("foo/baz")
    String baz();

}
