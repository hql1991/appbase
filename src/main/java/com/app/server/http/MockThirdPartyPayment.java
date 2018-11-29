package com.app.server.http;

import com.app.server.models.Payment;
import com.app.server.services.MockThirdPartyPaymentService;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("mockPayment")
public class MockThirdPartyPayment {
    private MockThirdPartyPaymentService service = MockThirdPartyPaymentService.getInstance();

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @POST
    @Path("pay")
    @Produces({MediaType.APPLICATION_JSON})
    public Payment mockPayment(Object request) {
        return service.create(request);
    }

//    @DELETE
//    @Path("cancel")
//    @Produces({MediaType.APPLICATION_JSON})
//    public APPResponse delete() {
//
//        return new APPResponse(service.cancel());
//    }

}
