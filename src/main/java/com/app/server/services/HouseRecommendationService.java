package com.app.server.services;

import com.app.server.models.HouseRecommendation;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Services run as singletons
 */

public class HouseRecommendationService {

    private static HouseRecommendationService self;
    private ObjectWriter ow;
    private MongoCollection<Document> recommendationCollection = null;

    private HouseRecommendationService() {
        this.recommendationCollection = MongoPool.getInstance().getCollection("houserecommendations");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static HouseRecommendationService getInstance() {
        if (self == null)
            self = new HouseRecommendationService();
        return self;
    }

    public ArrayList<HouseRecommendation> getAll() {
        ArrayList<HouseRecommendation> recommendationList = new ArrayList<HouseRecommendation>();

        FindIterable<Document> results = this.recommendationCollection.find();
        if (results == null) {
            return recommendationList;
        }
        for (Document item : results) {
            HouseRecommendation recommendation = convertDocumentToHouseRecommendation(item);
            recommendationList.add(recommendation);
        }
        return recommendationList;
    }

    // Get all recommendations of a given user
    public HouseRecommendation getAllHouseeRecommendationsOf(String renterId) {
//        ArrayList<HouseRecommendation> recommendationList = new ArrayList<HouseRecommendation>();

        BasicDBObject query = new BasicDBObject();
        query.put("renterId", renterId);

        Document item = recommendationCollection.find(query).first();
        if (item == null) {
            return null;
        }

        return convertDocumentToHouseRecommendation(item);
    }

    public HouseRecommendation getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = recommendationCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToHouseRecommendation(item);
    }


    public HouseRecommendation create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            HouseRecommendation recommendation = convertJsonToHouseRecommendation(json);
            Document doc = convertHouseRecommendationToDocument(recommendation);
            recommendationCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            recommendation.setId(id.toString());
            return recommendation;
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
            if (json.has("house_recolist"))
                doc.append("house_recolist", json.getJSONArray("house_recolist"));


            Document set = new Document("$set", doc);
            return recommendationCollection.updateOne(query, set);
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

        return recommendationCollection.deleteOne(query);

//        return new JSONObject();
    }


    public Object deleteAll() {

        recommendationCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private HouseRecommendation convertDocumentToHouseRecommendation(Document item) {

        List<String> house_recolist;
        house_recolist = (List<String>) item.get("house_recolist");
        HouseRecommendation recommendation = new HouseRecommendation(
                item.getString("renterId"),
                house_recolist
        );
        recommendation.setId(item.getObjectId("_id").toString());
        return recommendation;
    }

    private Document convertHouseRecommendationToDocument(HouseRecommendation recommendation) {
        List<BasicDBObject> recolist = new ArrayList<>();
        for (String reco :
                recommendation.getHouse_recolist()) {
            recolist.add(new BasicDBObject("houseId", reco));
        }

        Document doc = new Document("renterId", recommendation.getrenterId())
                .append("house_recolist", recolist);
        return doc;
    }

    private HouseRecommendation convertJsonToHouseRecommendation(JSONObject json) {
        HouseRecommendation recommendation = null;
        List<String> house_recolist = new ArrayList<>();
//        try {
        JSONArray recolist = json.getJSONArray("house_recolist");
        for (Object o : recolist) {
            house_recolist.add(o.toString());
        }
        recommendation = new HouseRecommendation(json.getString("renterId"),house_recolist);
//        } catch (ParseException e) {
//            System.out.println("Failed to convert Json to recommendation");
//            return null;
//        }
        return recommendation;
    }

}

