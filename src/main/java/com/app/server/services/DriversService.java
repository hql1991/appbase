package com.app.server.services;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.models.Driver;
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

/**
 * Services run as singletons
 */

public class DriversService {

    private static DriversService self;
    private ObjectWriter ow;
    private MongoCollection<Document> driversCollection = null;

    private DriversService() {
        this.driversCollection = MongoPool.getInstance().getCollection("drivers");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static DriversService getInstance(){
        if (self == null)
            self = new DriversService();
        return self;
    }

    public ArrayList<Driver> getAll() {
        ArrayList<Driver> driverList = new ArrayList<Driver>();

        FindIterable<Document> results = this.driversCollection.find();
        if (results == null) {
            return driverList;
        }
        for (Document item : results) {
            Driver driver = convertDocumentToDriver(item);
            driverList.add(driver);
        }
        return driverList;
    }

    public Driver getOne(String id) {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document item = driversCollection.find(query).first();
            if (item == null) {
                return null;
            }
            return convertDocumentToDriver(item);
    }

    public Driver create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Driver driver = convertJsonToDriver(json);
            Document doc = convertDriverToDocument(driver);
            driversCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            driver.setId(id.toString());
            return driver;
        } catch(JsonProcessingException e) {
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
                doc.append("firstName",json.getString("firstName"));
            if (json.has("middleName"))
                doc.append("middleName",json.getString("middleName"));
            if (json.has("lastName"))
                doc.append("lastName",json.getString("lastName"));
            if (json.has("address1"))
                doc.append("address1",json.getString("address1"));
            if (json.has("address2"))
                doc.append("address2",json.getString("address2"));
            if (json.has("city"))
                doc.append("city",json.getString("city"));
            if (json.has("state"))
                doc.append("state",json.getString("state"));
            if (json.has("country"))
                doc.append("country",json.getString("country"));
            if (json.has("postalCode"))
                doc.append("postalCode",json.getString("postalCode"));

            Document set = new Document("$set", doc);
            driversCollection.updateOne(query,set);
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

        driversCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        driversCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Driver convertDocumentToDriver(Document item) {
        Driver driver = new Driver(
                item.getString("firstName"),
                item.getString("middleName"),
                item.getString("lastName"),
                item.getString("address1"),
                item.getString("address2"),
                item.getString("city"),
                item.getString("state"),
                item.getString("country"),
                item.getString("postalCode")
        );
        driver.setId(item.getObjectId("_id").toString());
        return driver;
    }

    private Document convertDriverToDocument(Driver driver){
        Document doc = new Document("firstName", driver.getFirstName())
                .append("middleName", driver.getMiddleName())
                .append("lastName", driver.getLastName())
                .append("address1", driver.getAddress1())
                .append("address2", driver.getAddress2())
                .append("city", driver.getCity())
                .append("state", driver.getState())
                .append("country", driver.getCountry())
                .append("postalCode", driver.getPostalCode());
        return doc;
    }

    private Driver convertJsonToDriver(JSONObject json){
        Driver driver = new Driver( json.getString("firstName"),
                json.getString("middleName"),
                json.getString("lastName"),
                json.getString("address1"),
                json.getString("address2"),
                json.getString("city"),
                json.getString("state"),
                json.getString("country"),
                json.getString("postalCode"));
        return driver;
    }




} // end of main()
