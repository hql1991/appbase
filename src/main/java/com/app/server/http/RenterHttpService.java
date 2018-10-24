package com.app.server.http;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.Photo;
import com.app.server.models.Renter;
import com.app.server.services.PhotoService;
import com.app.server.services.RenterService;
import org.bson.types.ObjectId;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("renters")
public class RenterHttpService {
    private RenterService service;

    public RenterHttpService() {
        service = RenterService.getInstance();

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
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("id") String id) {
        try {
            Renter renter = service.getOne(id);
            if (renter == null)
                throw new APPNotFoundException(56, "Renter not found");
            return new APPResponse(renter);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "Renter not found");
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

    //Subresource as subclass
    @Path("renters/{renterId}/photos")
    public static class PhotoHttpService {
        private PhotoService service = PhotoService.getInstance();

        @OPTIONS
        @PermitAll
        public Response optionsById() {
            return Response.ok().build();
        }

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getAll(@PathParam("renterId") String renterId) {

            return new APPResponse(service.getAllPhotosOf(renterId));
        }

        @GET
        @Path("{id}")
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getOne(@PathParam("renterId") String renterId, @PathParam("id") String id) {
            try {
                Photo photo = service.getOnePhotoOf(renterId, id);
                if (photo == null)
                    throw new APPNotFoundException(56, "Photo not found");
                return new APPResponse(photo);
            } catch (IllegalArgumentException e) {
                throw new APPNotFoundException(56, "Photo not found");
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
}

