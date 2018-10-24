package com.app.server.services;

import com.app.server.models.Photo;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Services run as singletons
 */

public class PhotoService {

    private static PhotoService self;
    private final SimpleDateFormat simpleDateFormat;
    private ObjectWriter ow;
    private MongoCollection<Document> photoCollection = null;

    private PhotoService() {
        this.photoCollection = MongoPool.getInstance().getCollection("photos");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static PhotoService getInstance(){
        if (self == null)
            self = new PhotoService();
        return self;
    }

    public ArrayList<Photo> getAll() {
        ArrayList<Photo> photoList = new ArrayList<Photo>();

        FindIterable<Document> results = this.photoCollection.find();
        if (results == null) {
            return photoList;
        }
        for (Document item : results) {
            Photo photo = convertDocumentToPhoto(item);
            photoList.add(photo);
        }
        return photoList;
    }

    public Photo getOne(String id) {
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document item = photoCollection.find(query).first();
            if (item == null) {
                return null;
            }
            return convertDocumentToPhoto(item);
    }

    public Photo create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Photo photo = convertJsonToPhoto(json);
            Document doc = convertPhotoToDocument(photo);
            photoCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            photo.setId(id.toString());
            return photo;
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
            if (json.has("url"))
                doc.append("url",json.getString("url"));
            if (json.has("createDate"))
                doc.append("createDate",json.getString("createDate"));
            if (json.has("editDate"))
                doc.append("editDate",json.getString("editDate"));
            if (json.has("userId"))
                doc.append("userId",json.getString("userId"));

            Document set = new Document("$set", doc);
            photoCollection.updateOne(query,set);
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

        photoCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        photoCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Photo convertDocumentToPhoto(Document item) {
        Photo photo = new Photo(
                item.getString("url"),
                item.getDate("createDate"),
                item.getDate("editDate"),
                item.getString("userId")
        );
        photo.setId(item.getObjectId("_id").toString());
        return photo;
    }

    private Document convertPhotoToDocument(Photo photo){
        Document doc = new Document("url", photo.getUrl())
                .append("createDate", photo.getCreateDate())
                .append("editDate", photo.getEditDate())
                .append("userId", photo.getUserId());
        return doc;
    }

    private Photo convertJsonToPhoto(JSONObject json){
        Photo photo = null;
        try {
            photo = new Photo( json.getString("url"),
                    simpleDateFormat.parse(json.getString("createDate")),
                    simpleDateFormat.parse(json.getString("editDate")),
                    json.getString("userId"));
        } catch (ParseException e) {
            System.out.println("Failed to convert Json to photo");
            return null;
        }
        return photo;
    }




} // end of main()
