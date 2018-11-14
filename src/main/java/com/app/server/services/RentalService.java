package com.app.server.services;

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
import com.app.server.models.Rental;

import java.util.ArrayList;

/**
 * Services run as singletons
 */

public class RentalService {

    private static RentalService self;
    private ObjectWriter ow;
    private MongoCollection<Document> rentalCollection = null;

    private RentalService() {
        this.rentalCollection = MongoPool.getInstance().getCollection("rentals");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static RentalService getInstance() {
        if (self == null)
            self = new RentalService();
        return self;
    }

    public ArrayList<Rental> getAll() {
        ArrayList<Rental> rentalList = new ArrayList<Rental>();

        FindIterable<Document> results = this.rentalCollection.find();
        if (results == null) {
            return rentalList;
        }
        for (Document item : results) {
            Rental rental = convertDocumentToRental(item);
            rentalList.add(rental);
        }
        return rentalList;
    }

    // Get all renter rentals of a given user
    public ArrayList<Rental> getAllRentalsOfRenter(String userId) {
        ArrayList<Rental> rentalList = new ArrayList<Rental>();

        BasicDBObject query = new BasicDBObject();
        query.put("renterId", userId);

        FindIterable<Document> results = this.rentalCollection.find(query);
        if (results == null) {
            return rentalList;
        }
        for (Document item : results) {
            Rental rental = convertDocumentToRental(item);
            rentalList.add(rental);
        }
        return rentalList;
    }

    // Get all owner rentals of a given user
    public ArrayList<Rental> getAllRentalsOfOwner(String ownerId) {
        ArrayList<Rental> rentalList = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("ownerId", ownerId);

        FindIterable<Document> results = this.rentalCollection.find(query);
        if (results == null) {
            return rentalList;
        }
        for (Document item : results) {
            Rental rental = convertDocumentToRental(item);
            rentalList.add(rental);
        }
        return rentalList;
    }

    public Rental getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = rentalCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToRental(item);
    }


    public Rental create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Rental rental = convertJsonToRental(json);
            Document doc = convertRentalToDocument(rental);
            rentalCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            rental.setId(id.toString());
            return rental;
        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
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
            if (json.has("renterId"))
                doc.append("renterId", json.getString("renterId"));
            if (json.has("ownerId"))
                doc.append("ownerId", json.getString("ownerId"));
            if (json.has("startDate"))
                doc.append("startDate", json.getString("startDate"));
            if (json.has("endDate"))
                doc.append("endDate", json.getString("endDate"));
            if (json.has("timeStamp"))
                doc.append("timeStamp", json.getString("timeStamp"));

            Document set = new Document("$set", doc);
            return rentalCollection.updateOne(query, set);
//            return request;

        } catch (JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }


    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        return rentalCollection.deleteOne(query);

//        return new JSONObject();
    }


    public Object deleteAll() {

        rentalCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Rental convertDocumentToRental(Document item) {
        Rental rental = new Rental(
                item.getString("renterId"),
                item.getString("ownerId"),
                item.getString("startDate"),
                item.getString("endDate").toString(),
                item.getString("timeStamp").toString()
        );
        rental.setId(item.getObjectId("_id").toString());
        return rental;
    }

    private Document convertRentalToDocument(Rental rental) {
        Document doc = new Document("renterId", rental.getRenterId())
                .append("ownerId", rental.getOwnerId())
                .append("startDate", rental.getStartDate())
                .append("endDate", rental.getEndDate())
                .append("timeStamp", rental.getTimeStamp());
        return doc;
    }

    private Rental convertJsonToRental(JSONObject json) {
        Rental rental = null;
        try {
            rental = new Rental(json.getString("renterId"),
                    json.getString("ownerId"),
                    json.getString("startDate"),
                    json.getString("endDate"),
                    json.getString("timeStamp"));

        } catch (Exception e) {
            System.out.println("Failed to convert Json to appointment");
            return null;
        }
        return rental;
    }

}
