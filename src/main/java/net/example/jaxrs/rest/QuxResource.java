package net.example.jaxrs.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface QuxResource {

    @POST
    String some(String args);

    @GET
    @Path("qux")
    String quux();

}
