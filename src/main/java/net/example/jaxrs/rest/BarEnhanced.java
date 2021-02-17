package net.example.jaxrs.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("rest/foo")
public interface BarEnhanced {

    @GET
    @Path("bar")
    String bar();

    @GET
    @Path("baz")
    String baz();

    @DELETE
    String fooBar();

    @Path("")
    QuxResource qux();

    @POST
    @Path("qux")
    String ohoo(String hello);

}
