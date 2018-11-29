package com.app.server.services;

import com.app.server.http.utils.APPCrypt;
import com.app.server.models.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Services run as singletons
 */

public class MockThirdPartyPaymentService {

    private static MockThirdPartyPaymentService self;
    private ObjectWriter ow;

    private MockThirdPartyPaymentService() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static MockThirdPartyPaymentService getInstance() {
        if (self == null)
            self = new MockThirdPartyPaymentService();
        return self;
    }

    public Payment create(Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Payment payment = PaymentService.convertJsonToPayment(json);

            // mocks a payment gateway that returns a transaction id.
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String transactionId = APPCrypt.encrypt(String.valueOf(ts.getTime()));
            payment.setTransactionId(transactionId);
            return payment;
        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
