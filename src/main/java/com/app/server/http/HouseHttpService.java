package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.House;
import com.app.server.services.HouseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/owners/{ownerid}/houses")

public class HouseHttpService {
    private HouseService service;
    private ObjectWriter ow;

    public HouseHttpService() {
        service = HouseService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @GET
//    @Path("/owners/{ownerid}/houses")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getAll(@PathParam("ownerid") String ownerid) {

        return new APPResponse(service.getAll(ownerid));
    }

    @GET
    @Path("{houseid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid) {
        try {
            House d = service.getOne(ownerid, houseid);
            if (d == null)
                throw new APPNotFoundException(56,"Owner not found");
            return new APPResponse(service.getOne(ownerid, houseid));
        }
        catch(IllegalArgumentException e){
            throw new APPNotFoundException(56,"Owner not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0,"Something happened. Come back later.");
        }

    }

    @POST
//    @Path("/owners/{ownerid}/houses")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse create(@PathParam("ownerid") String ownerid, Object request) {
        return new APPResponse(service.create(request));
    }
    //didn't do input validation

    @PATCH
    @Path("{houseid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid, Object request){

        return new APPResponse(service.update(ownerid, houseid,request));

    }

    @DELETE
    @Path("{houseid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid) {

        return new APPResponse(service.delete(ownerid, houseid));
    }

    @DELETE
//    @Path("/owners/{ownerid}/houses")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("ownerid") String ownerid) {

        return new APPResponse(service.deleteAll(ownerid));
    }
}
