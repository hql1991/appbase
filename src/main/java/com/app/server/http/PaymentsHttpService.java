package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.Payment;
import com.app.server.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("payments")

public class PaymentsHttpService {
    private PaymentService service;
    private ObjectWriter ow;

    public PaymentsHttpService() {
        service = PaymentService.getInstance();
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
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("id") String id) {
        try {
            Payment d = service.getOne(id);
            if (d == null)
                throw new APPNotFoundException(56, "Payment not found");
            return new APPResponse(service.getOne(id));
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "Payment not found");
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
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("id") String id, Object request) {

        return new APPResponse(service.update(id, request));

    }

    @DELETE
    @Path("{transactionId}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("transactionId") String transactionId) {

        return new APPResponse(service.delete(transactionId));
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

}
