package com.app.server.services;

import com.app.server.http.utils.Hash;
import com.app.server.models.Owner;

import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OwnersService {

    private static OwnersService self;
    private ObjectWriter ow;
    private MongoCollection<Document> ownersCollection = null;

    private OwnersService() {
        this.ownersCollection = MongoPool.getInstance().getCollection("owners");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static OwnersService getInstance() {
        if (self == null)
            self = new OwnersService();
        return self;
    }

    public ArrayList<Owner> getAll() {
        ArrayList<Owner> ownerList = new ArrayList<Owner>();

        FindIterable<Document> results = this.ownersCollection.find();
        if (results == null) {
            return ownerList;
        }
        for (Document item : results) {
            Owner owner = convertDocumentToOwner(item);
            ownerList.add(owner);
        }
        return ownerList;
    }

    public Owner getOne(String ownerid) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(ownerid));

        Document item = ownersCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToOwner(item);
    }

    public Owner create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Owner owner = convertJsonToOwner(json);
            Document doc = convertOwnerToDocument(owner);
            ownersCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            owner.setId(id.toString());
            return owner;
        } catch (JsonProcessingException e) {
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
            if (json.has("userName"))
                doc.append("userName", json.getString("userName"));
            if (json.has("firstName"))
                doc.append("firstName", json.getString("firstName"));
            if (json.has("lastName"))
                doc.append("lastName", json.getString("lastName"));
            if (json.has("prefGender"))
                doc.append("prefGender", json.getString("prefGender"));
            if (json.has("prefJob"))
                doc.append("prefJob", json.getString("prefJob"));
            if (json.has("prefNum"))
                doc.append("prefNum", json.getInt("prefNum"));
            if (json.has("prefCook"))
                doc.append("prefCook", json.getString("prefCook"));
            if (json.has("email"))
                doc.append("email", json.getString("email"));
            if (json.has("password"))
                doc.append("password", json.getString("password"));

            Document set = new Document("$set", doc);
            ownersCollection.updateOne(query, set);
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

        ownersCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        ownersCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    public static Owner convertDocumentToOwner(Document item) {
        Owner owner = new Owner(
                item.getString("userName"),
                item.getString("firstName"),
                item.getString("lastName"),
                item.getString("prefGender"),
                item.getString("prefJob"),
                item.getInteger("prefNum"),
                item.getString("prefCook"),
                item.getString("email"),
                item.getString("password"),
                item.get("salt", Binary.class).getData()
        );
        owner.setId(item.getObjectId("_id").toString());
        return owner;
    }

    private Document convertOwnerToDocument(Owner owner) throws Exception {
        byte[] salt = Hash.generateSalt();
        String saltedHash = Hash.PBKDF2Hash(owner.getPassword(), salt);

        Document doc = new Document("userName", owner.getUserName())
                .append("firstName", owner.getFirstName())
                .append("lastName", owner.getLastName())
                .append("prefGender", owner.getPrefGender())
                .append("prefJob", owner.getPrefJob())
                .append("prefNum", owner.getPrefNum())
                .append("prefCook", owner.getPrefCook())
                .append("email", owner.getEmail())
                .append("password", saltedHash)
                .append("salt", salt);
        return doc;
    }

    private Owner convertJsonToOwner(JSONObject json) {
        Owner owner = new Owner(
                json.getString("userName"),
                json.getString("firstName"),
                json.getString("lastName"),
                json.getString("prefGender"),
                json.getString("prefJob"),
                json.getInt("prefNum"),
                json.getString("prefCook"),
                json.getString("email"),
                json.getString("password"),
                null);
        return owner;
    }


}
