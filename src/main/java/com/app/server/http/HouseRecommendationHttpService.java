package com.app.server.http;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.HouseRecommendation;
import com.app.server.services.HouseRecommendationService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rec")
public class HouseRecommendationHttpService {
    private HouseRecommendationService service;

    public HouseRecommendationHttpService() {
        service = HouseRecommendationService.getInstance();
    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }

    @GET
    @Path("renters")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getAll() {
        return new APPResponse(service.getAll());
    }

    @GET
    @Path("renters/{renterId}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("renterId") String renterId) {
        try {
            HouseRecommendation roomReco = service.getAllHouseRecommendationsOf(renterId);
            if (roomReco == null)
                throw new APPNotFoundException(56, "HouseRecommendation not found");
            return new APPResponse(roomReco);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "HouseRecommendation not found");
        }
//        catch (Exception e) {
//            throw new APPInternalServerException(0, "Something happened. Come back later.");
//        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) {
        return new APPResponse(service.create(request));
    }

    @PATCH
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("id") String id, Object request) {

        return new APPResponse(service.update(id, request));

    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("id") String id) {

        return new APPResponse(service.delete(id));
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

}
