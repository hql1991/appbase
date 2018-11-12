package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.services.HouseService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("houses")
public class HouseHttpService {
    private HouseService service;

    public HouseHttpService() {
        service = HouseService.getInstance();
    }


    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getAll() {

        return new APPResponse(service.getAll());
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

}
