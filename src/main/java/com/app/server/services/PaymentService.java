package com.app.server.services;

import com.app.server.http.utils.APPCrypt;
import com.app.server.models.Payment;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentService {

    private static PaymentService self;
    private ObjectWriter ow;
    private MongoCollection<Document> paymentsCollection = null;

    private PaymentService() {
        this.paymentsCollection = MongoPool.getInstance().getCollection("payments");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static PaymentService getInstance(){
        if (self == null)
            self = new PaymentService();
        return self;
    }

    public ArrayList<Payment> getAll() {
        ArrayList<Payment> paymentList = new ArrayList<Payment>();

        FindIterable<Document> results = this.paymentsCollection.find();
        if (results == null) {
            return paymentList;
        }
        for (Document item : results) {
            Payment payment = convertDocumentToPayment(item);
            paymentList.add(payment);
        }
        return paymentList;
    }

    public Payment getOne(String paymentid) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(paymentid));

        Document item = paymentsCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToPayment(item);
    }

    public Payment create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Payment payment = convertJsonToPayment(json);
            Document doc = convertPaymentToDocument(payment);
            paymentsCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            payment.setId(id.toString());
            return payment;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        } catch (Exception e) {
            System.out.println("Failed to create a document" + e.getMessage());
            return null;
        }
    }


    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("transactionId"))
                doc.append("transactionId",json.getString("transactionId"));
            if (json.has("amount"))
                doc.append("amount",json.getString("amount"));
            if (json.has("rentalId"))
                doc.append("rentalId",json.getString("rentalId"));

            Document set = new Document("$set", doc);
            paymentsCollection.updateOne(query,set);
            return request;

        } catch(JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        }
        catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }




    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        paymentsCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        paymentsCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Payment convertDocumentToPayment(Document item) {
        Payment payment = new Payment(
                item.getString("transactionId"),
                item.getString("amount"),
                item.getString("rentalId")

        );
        payment.setId(item.getObjectId("_id").toString());
        return payment;
    }

    private Document convertPaymentToDocument(Payment payment) throws Exception{
        Document doc = new Document("transactionId", payment.getTransactionId())
                .append("amount", payment.getAmount())
                .append("rentalId", payment.getRentalId());
        return doc;
    }

    private Payment convertJsonToPayment(JSONObject json){
        Payment payment = new Payment(
                json.getString("transactionId"),
                json.getString("amount"),
                json.getString("rentalId"));
        return payment;
    }


}
