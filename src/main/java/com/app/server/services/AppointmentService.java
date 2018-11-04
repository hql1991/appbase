package com.app.server.services;

import com.app.server.models.Appointment;
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

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Services run as singletons
 */

public class AppointmentService {

    private static AppointmentService self;
    private final SimpleDateFormat simpleDateFormat;
    private ObjectWriter ow;
    private MongoCollection<Document> appointmentCollection = null;

    private AppointmentService() {
        this.appointmentCollection = MongoPool.getInstance().getCollection("appointments");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public static AppointmentService getInstance() {
        if (self == null)
            self = new AppointmentService();
        return self;
    }

    public ArrayList<Appointment> getAll() {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();

        FindIterable<Document> results = this.appointmentCollection.find();
        if (results == null) {
            return appointmentList;
        }
        for (Document item : results) {
            Appointment appointment = convertDocumentToAppointment(item);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    // Get all appointments of a given user, sent or received
    public ArrayList<Appointment> getAllAppointmentsOf(String userId) {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        appointmentList = getAllSentAppointmentsOf(userId);
        appointmentList.addAll(getAllReceivedAppointmentsOf(userId));
        return appointmentList;
    }

    // Get all sent appointments of a given user
    public ArrayList<Appointment> getAllSentAppointmentsOf(String userId) {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();

        BasicDBObject query = new BasicDBObject();
        query.put("senderId", userId);

        FindIterable<Document> results = this.appointmentCollection.find(query);
        if (results == null) {
            return appointmentList;
        }
        for (Document item : results) {
            Appointment appointment = convertDocumentToAppointment(item);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    // Get all received appointments of a given user
    public ArrayList<Appointment> getAllReceivedAppointmentsOf(String userId) {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();

        BasicDBObject query = new BasicDBObject();
        query.put("receiverId", userId);

        FindIterable<Document> results = this.appointmentCollection.find(query);
        if (results == null) {
            return appointmentList;
        }
        for (Document item : results) {
            Appointment appointment = convertDocumentToAppointment(item);
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    public Appointment getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = appointmentCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToAppointment(item);
    }

//    // Get one Appointment of a given user
//    public Appointment getOneAppointmentOf(String userId, String id) {
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(id));
//        //Todo: query senderId or receiverId
//        query.put("senderId", userId);
//
//        Document item = appointmentCollection.find(query).first();
//        if (item == null) {
//            return null;
//        }
//        return convertDocumentToAppointment(item);
//    }

    public Appointment create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Appointment appointment = convertJsonToAppointment(json);
            Document doc = convertAppointmentToDocument(appointment);
            appointmentCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            appointment.setId(id.toString());
            return appointment;
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
            if (json.has("detail"))
                doc.append("detail", json.getString("detail"));
            if (json.has("senderId"))
                doc.append("senderId", json.getString("senderId"));
            if (json.has("receiverId"))
                doc.append("receiverId", json.getString("receiverId"));
            if (json.has("meetTime"))
                doc.append("meetTime", json.getString("meetTime"));
            if (json.has("accepted"))
                doc.append("accepted", json.getString("accepted"));

            Document set = new Document("$set", doc);
            return appointmentCollection.updateOne(query, set);
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

        return appointmentCollection.deleteOne(query);

//        return new JSONObject();
    }


    public Object deleteAll() {

        appointmentCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Appointment convertDocumentToAppointment(Document item) {
        Appointment appointment = new Appointment(
                item.getString("detail"),
                item.getString("senderId"),
                item.getString("receiverId"),
                item.getDate("meetTime"),
                item.getBoolean("accepted")
        );
        appointment.setId(item.getObjectId("_id").toString());
        return appointment;
    }

    private Document convertAppointmentToDocument(Appointment appointment) {
        Document doc = new Document("detail", appointment.getDetail())
                .append("senderId", appointment.getSenderId())
                .append("receiverId", appointment.getReceiverId())
                .append("meetTime", appointment.getMeetTime())
                .append("accepted", appointment.isAccepted());
        return doc;
    }

    private Appointment convertJsonToAppointment(JSONObject json) {
        Appointment appointment = null;
        try {
            appointment = new Appointment(json.getString("detail"),
                    json.getString("senderId"),
                    json.getString("receiverId"),
                    simpleDateFormat.parse(json.getString("meetTime")),
                    json.getBoolean("accepted"));
        } catch (ParseException e) {
            System.out.println("Failed to convert Json to appointment");
            return null;
        }
        return appointment;
    }

}
