package net.example.jaxrs.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("unmatched")
public interface UnmatchedResource {

    @GET
    String duh();

}
