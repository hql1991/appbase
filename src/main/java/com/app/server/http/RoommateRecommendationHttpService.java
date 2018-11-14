package com.app.server.http;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.RoommateRecommendation;
import com.app.server.services.RoommateRecommendationService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rec/renters")
public class RoommateRecommendationHttpService {
    private RoommateRecommendationService service;

    public RoommateRecommendationHttpService() {
        service = RoommateRecommendationService.getInstance();
    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }

    @GET
//    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getAll() {
        return new APPResponse(service.getAll());
    }

    @GET
    @Path("{renterId}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("renterId") String renterId) {
        try {
            RoommateRecommendation roomReco = service.getAllRoommateRecommendationsOf(renterId);
            if (roomReco == null)
                throw new APPNotFoundException(56, "RoommateRecommendation not found");
            return new APPResponse(roomReco);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "RoommateRecommendation not found");
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
