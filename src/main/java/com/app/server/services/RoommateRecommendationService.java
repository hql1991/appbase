package com.app.server.services;

import com.app.server.models.RoommateRecommendation;
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

public class RoommateRecommendationService {

    private static RoommateRecommendationService self;
    private ObjectWriter ow;
    private MongoCollection<Document> recommendationCollection = null;

    private RoommateRecommendationService() {
        this.recommendationCollection = MongoPool.getInstance().getCollection("roommaterecommendations");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static RoommateRecommendationService getInstance() {
        if (self == null)
            self = new RoommateRecommendationService();
        return self;
    }

    public ArrayList<RoommateRecommendation> getAll() {
        ArrayList<RoommateRecommendation> recommendationList = new ArrayList<RoommateRecommendation>();

        FindIterable<Document> results = this.recommendationCollection.find();
        if (results == null) {
            return recommendationList;
        }
        for (Document item : results) {
            RoommateRecommendation recommendation = convertDocumentToRoommateRecommendation(item);
            recommendationList.add(recommendation);
        }
        return recommendationList;
    }

    // Get all recommendations of a given user
    public RoommateRecommendation getAllRoommateRecommendationsOf(String renterId) {
//        ArrayList<RoommateRecommendation> recommendationList = new ArrayList<RoommateRecommendation>();

        BasicDBObject query = new BasicDBObject();
        query.put("renterId", renterId);

        Document item = recommendationCollection.find(query).first();
        if (item == null) {
            return null;
        }
//        for (Document item : results) {
//            RoommateRecommendation recommendation = convertDocumentToRoommateRecommendation(item);
//            recommendationList.add(recommendation);
//        }
        return convertDocumentToRoommateRecommendation(item);
    }

    public RoommateRecommendation getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = recommendationCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToRoommateRecommendation(item);
    }

//    // Get one RoommateRecommendation of a given user
//    public RoommateRecommendation getOneRoommateRecommendationOf(String userId, String id) {
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(id));
//        query.put("userId", userId);
//
//        Document item = recommendationCollection.find(query).first();
//        if (item == null) {
//            return null;
//        }
//        return convertDocumentToRoommateRecommendation(item);
//    }

    public RoommateRecommendation create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            RoommateRecommendation recommendation = convertJsonToRoommateRecommendation(json);
            Document doc = convertRoommateRecommendationToDocument(recommendation);
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
            if (json.has("roommate_recolist"))
                doc.append("roommate_recolist", json.getJSONArray("roommate_recolist"));


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

    private RoommateRecommendation convertDocumentToRoommateRecommendation(Document item) {

        List<String> roommate_recolist;
        roommate_recolist = (List<String>) item.get("roommate_recolist");
        RoommateRecommendation recommendation = new RoommateRecommendation(
                item.getString("renterId"),
                roommate_recolist
        );
        recommendation.setId(item.getObjectId("_id").toString());
        return recommendation;
    }

    private Document convertRoommateRecommendationToDocument(RoommateRecommendation recommendation) {
        List<BasicDBObject> recolist = new ArrayList<>();
        for (String reco :
                recommendation.getRoommate_recolist()) {
            recolist.add(new BasicDBObject("roomId", reco));
        }

        Document doc = new Document("renterId", recommendation.getRenterId())
                .append("roommate_recolist", recolist);
        return doc;
    }

    private RoommateRecommendation convertJsonToRoommateRecommendation(JSONObject json) {
        RoommateRecommendation recommendation = null;
        List<String> roommate_recolist = new ArrayList<>();
//        try {
            JSONArray recolist = json.getJSONArray("roommate_recolist");
            for (Object o : recolist) {
                roommate_recolist.add(o.toString());
            }
            recommendation = new RoommateRecommendation(json.getString("renterId"),roommate_recolist);
//        } catch (ParseException e) {
//            System.out.println("Failed to convert Json to recommendation");
//            return null;
//        }
        return recommendation;
    }

}
