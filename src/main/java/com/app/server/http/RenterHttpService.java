package com.app.server.http;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.Photo;
import com.app.server.models.Renter;
import com.app.server.services.*;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
        private PhotoService service;


        public PhotoHttpService(@PathParam("renterId") String renterId, @Context HttpHeaders httpHeaders) {
            this.service = PhotoService.getInstance();

            SessionService.validateToken(renterId, httpHeaders);
        }

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

    @Path("renters/{renterId}/appointments")
    public static class AppointmentHttpService {
        private AppointmentService service;

        public AppointmentHttpService(@PathParam("renterId") String renterId, @Context HttpHeaders httpHeaders) {
            this.service = AppointmentService.getInstance();

            SessionService.validateToken(renterId, httpHeaders);
        }

        @OPTIONS
        @PermitAll
        public Response optionsById() {
            return Response.ok().build();
        }

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getAll(@PathParam("renterId") String renterId) {

            return new APPResponse(service.getAllAppointmentsOf(renterId));
        }

        @GET
        @Path("sent")
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getAllSent(@PathParam("renterId") String renterId) {

            return new APPResponse(service.getAllSentAppointmentsOf(renterId));
        }

        @GET
        @Path("received")
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getAllReceived(@PathParam("renterId") String renterId) {

            return new APPResponse(service.getAllReceivedAppointmentsOf(renterId));
        }

//        @GET
//        @Path("{id}")
//        @Produces({MediaType.APPLICATION_JSON})
//        public APPResponse getOne(@PathParam("renterId") String renterId, @PathParam("id") String id) {
//            try {
//                Appointment appointment = service.getOneAppointmentOf(renterId, id);
//                if (appointment == null)
//                    throw new APPNotFoundException(56, "Appointment not found");
//                return new APPResponse(appointment);
//            } catch (IllegalArgumentException e) {
//                throw new APPNotFoundException(56, "Appointment not found");
//            }
//            //        catch (Exception e) {
//            //            throw new APPInternalServerException(0, "Something happened. Come back later.");
//            //        }
//
//        }

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

    @Path("renters/{renterId}/rentals")
    public static class RentalHttpService {
        private RentalService service;

        public RentalHttpService(@PathParam("renterId") String renterId, @Context HttpHeaders httpHeaders) {
            this.service = RentalService.getInstance();

            SessionService.validateToken(renterId, httpHeaders);
        }

        @OPTIONS
        @PermitAll
        public Response optionsById() {
            return Response.ok().build();
        }

        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public APPResponse getAllRentalsOfRenter(@PathParam("renterId") String renterId) {

            return new APPResponse(service.getAllRentalsOfRenter(renterId));
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

