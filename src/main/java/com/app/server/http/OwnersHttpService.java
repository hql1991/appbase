package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.House;
import com.app.server.models.Owner;
import com.app.server.services.HouseService;
import com.app.server.services.OwnersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("owners")

public class OwnersHttpService {
    private OwnersService service;
    private ObjectWriter ow;

    public OwnersHttpService() {
        service = OwnersService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

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

    @GET
    @Path("{ownerid}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("ownerid") String ownerid) {
        try {
            Owner d = service.getOne(ownerid);
            if (d == null)
                throw new APPNotFoundException(56, "Owner not found");
            return new APPResponse(service.getOne(ownerid));
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "Owner not found");
        } catch (Exception e) {
            throw new APPInternalServerException(0, "Something happened. Come back later.");
        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) {
        return new APPResponse(service.create(request));
    }

    @PATCH
    @Path("{ownerid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("ownerid") String ownerid, Object request) {

        return new APPResponse(service.update(ownerid, request));

    }

    @DELETE
    @Path("{ownerid}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("ownerid") String ownerid) {

        return new APPResponse(service.delete(ownerid));
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }


    @Path("/owners/{ownerid}/houses")

    public static class HouseHttpService {
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
        public APPResponse getAll(@PathParam("ownerid") String ownerid) {

            return new APPResponse(service.getAllHousesOf(ownerid));
        }

        @GET
        @Path("{houseid}")
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getOne(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid) {
            try {
                House d = service.getOne(ownerid, houseid);
                if (d == null)
                    throw new APPNotFoundException(56, "Owner not found");
                return new APPResponse(service.getOne(ownerid, houseid));
            } catch (IllegalArgumentException e) {
                throw new APPNotFoundException(56, "Owner not found");
            } catch (Exception e) {
                throw new APPInternalServerException(0, "Something happened. Come back later.");
            }

        }

        @POST
        @Consumes({MediaType.APPLICATION_JSON})
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse create(@PathParam("ownerid") String ownerid, Object request) {
            return new APPResponse(service.create(request));
        }
        //didn't do input validation

        @PATCH
        @Path("{houseid}")
        @Consumes({MediaType.APPLICATION_JSON})
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse update(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid, Object request) {

            return new APPResponse(service.update(ownerid, houseid, request));

        }

        @DELETE
        @Path("{houseid}")
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse delete(@PathParam("ownerid") String ownerid, @PathParam("houseid") String houseid) {

            return new APPResponse(service.delete(ownerid, houseid));
        }

        @DELETE
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse delete(@PathParam("ownerid") String ownerid) {

            return new APPResponse(service.deleteAllHousesOf(ownerid));
        }
    }
}
