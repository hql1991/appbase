package com.app.server.services;

import com.app.server.models.House;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
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
import java.util.HashMap;

public class HouseService {

    private static HouseService self;
    private ObjectWriter ow;
    private MongoCollection<Document> houseCollection = null;

    private HouseService() {
        this.houseCollection = MongoPool.getInstance().getCollection("house");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static HouseService getInstance(){
        if (self == null)
            self = new HouseService();
        return self;
    }

    public ArrayList<House> getAllHousesOf(String ownerid) {

        ArrayList<House> houseList = new ArrayList<House>();

        BasicDBObject field = new BasicDBObject();
        field.put("ownerid", ownerid);

        FindIterable<Document> results = this.houseCollection.find(field);
        if (results == null) {
            return houseList;
        }
        for (Document item : results) {
            House house = convertDocumentToHouse(item);
            houseList.add(house);
        }
        return houseList;
    }
    public ArrayList<House> getAll() {

        ArrayList<House> houseList = new ArrayList<House>();

        FindIterable<Document> results = this.houseCollection.find();
        if (results == null) {
            return houseList;
        }
        for (Document item : results) {
            House house = convertDocumentToHouse(item);
            houseList.add(house);
        }
        return houseList;
    }

    public House getOne(String ownerid, String houseid) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(houseid));

        Document item = houseCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToHouse(item);
    }

    public House create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            House house = convertJsonToHouse(json);
            Document doc = convertHouseToDocument(house);
            houseCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            house.setId(id.toString());
            return house;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }


    public Object update(String ownerid, String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("ownerid"))
                doc.append("ownerid",json.getString("ownerid"));
            if (json.has("address"))
                doc.append("address",json.getString("address"));
            if (json.has("state"))
                doc.append("state",json.getString("state"));
            if (json.has("zipcode"))
                doc.append("zipcode",json.getString("zipcode"));
            if (json.has("year"))
                doc.append("year",json.getString("year"));

            Document set = new Document("$set", doc);
            houseCollection.updateOne(query,set);
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




    public Object delete(String ownerid, String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        houseCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAllHousesOf(String ownerid) {
        BasicDBObject query=new BasicDBObject();
        query.put("ownerId", ownerid);
        houseCollection.deleteMany(query);

        return new JSONObject();
    }

    public Object deleteAll() {

        houseCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private House convertDocumentToHouse(Document item) {
        House house = new House(
                item.getString("ownerid"),
                item.getString("address"),
                item.getString("state"),
                item.getString("zipcode"),
                item.getString("year")

        );
        house.setId(item.getObjectId("_id").toString());
        return house;
    }

    private Document convertHouseToDocument(House house){
        Document doc = new Document("ownerid", house.getOwnerid())
                .append("address", house.getaddress())
                .append("state", house.getstate())
                .append("zipcode", house.getzipcode())
                .append("year", house.getyear());
        return doc;
    }

    private House convertJsonToHouse(JSONObject json){
       House house = new House(
                json.getString("ownerid"),
                json.getString("address"),
                json.getString("state"),
                json.getString("zipcode"),
                json.getString("year"));

        return house;
    }

}
