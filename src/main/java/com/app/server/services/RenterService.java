package com.app.server.services;

import com.app.server.models.Renter;
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

/**
 * Services run as singletons
 */

public class RenterService {

    private static RenterService self;
    private ObjectWriter ow;
    private MongoCollection<Document> renterCollection = null;

    private RenterService() {
        this.renterCollection = MongoPool.getInstance().getCollection("renters");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static RenterService getInstance() {
        if (self == null)
            self = new RenterService();
        return self;
    }

    public ArrayList<Renter> getAll() {
        ArrayList<Renter> renterList = new ArrayList<Renter>();

        FindIterable<Document> results = this.renterCollection.find();
        if (results == null) {
            return renterList;
        }
        for (Document item : results) {
            Renter renter = convertDocumentToRenter(item);
            renterList.add(renter);
        }
        return renterList;
    }

    public Renter getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = renterCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToRenter(item);
    }

    public Renter create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Renter renter = convertJsonToRenter(json);
            Document doc = convertRenterToDocument(renter);
            renterCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            renter.setId(id.toString());
            return renter;
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
            if (json.has("firstName"))
                doc.append("firstName", json.getString("firstName"));
            if (json.has("lastName"))
                doc.append("lastName", json.getString("lastName"));
            if (json.has("email"))
                doc.append("email", json.getString("email"));
            if (json.has("gender"))
                doc.append("gender", json.getInt("gender"));
            if (json.has("job"))
                doc.append("job", json.getString("job"));
            if (json.has("num"))
                doc.append("num", json.getString("num"));
            if (json.has("cook"))
                doc.append("cook", json.getString("cook"));
            if (json.has("pref_gender"))
                doc.append("pref_gender", json.getString("pref_gender"));
            if (json.has("pref_job"))
                doc.append("pref_job", json.getString("pref_job"));
            if (json.has("pref_num"))
                doc.append("pref_num", json.getString("pref_num"));
            if (json.has("pref_cook"))
                doc.append("pref_cook", json.getString("pref_cook"));

            Document set = new Document("$set", doc);
            renterCollection.updateOne(query, set);
            return request;

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

        renterCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        renterCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Renter convertDocumentToRenter(Document item) {
        Renter renter = new Renter(
                item.getString("firstName"),
                item.getString("lastName"),
                item.getString("email"),
                item.getInteger("gender"),
                item.getInteger("job"),
                item.getInteger("num"),
                item.getInteger("cook"),
                item.getInteger("pref_gender"),
                item.getInteger("pref_job"),
                item.getInteger("pref_num"),
                item.getInteger("pref_cook")
        );

        renter.setId(item.getObjectId("_id").toString());
        return renter;
    }

    private Document convertRenterToDocument(Renter renter) {
        Document doc = new Document("firstName", renter.getFirstName())
                .append("lastName", renter.getLastName())
                .append("email", renter.getEmail())
                .append("gender", renter.getGender())
                .append("job", renter.getJob())
                .append("num", renter.getNum())
                .append("cook", renter.getCook())
                .append("pref_gender", renter.getPref_gender())
                .append("pref_job", renter.getPref_job())
                .append("pref_num", renter.getPref_num())
                .append("pref_cook", renter.getPref_cook());
        return doc;
    }

    private Renter convertJsonToRenter(JSONObject json) {
        Renter renter = new Renter(json.getString("firstName"),
                json.getString("lastName"),
                json.getString("email"),
                json.getInt("gender"),
                json.getInt("job"),
                json.getInt("num"),
                json.getInt("cook"),
                json.getInt("pref_gender"),
                json.getInt("pref_job"),
                json.getInt("pref_num"),
                json.getInt("pref_cook"));
        return renter;
    }


}
