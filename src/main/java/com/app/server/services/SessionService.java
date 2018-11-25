package com.app.server.services;

import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.models.Owner;
import com.app.server.models.Session;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Services run as singletons
 */

public class SessionService {

    private static SessionService self;
    private ObjectWriter ow;
    private MongoCollection<Document> ownersCollection = null;
    private MongoCollection<Document> houseCollection = null;

    private SessionService() {
        this.ownersCollection = MongoPool.getInstance().getCollection("owners");
        this.houseCollection = MongoPool.getInstance().getCollection("houses");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static SessionService getInstance(){
        if (self == null)
            self = new SessionService();
        return self;
    }

    public Session create(Object request) {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
            if (!json.has("emailAddress"))
                throw new APPBadRequestException(55, "missing emailAddress");
            if (!json.has("password"))
                throw new APPBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            query.put("emailAddress", json.getString("emailAddress"));
//            query.put("password", APPCrypt.encrypt(json.getString("password")));
            query.put("password", json.getString("password"));

            Document item = ownersCollection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No user found matching credentials");
            }

            Owner owner = convertDocumentToOwner(item);

            owner.setId(item.getObjectId("_id").toString());
            return new Session(owner);
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        catch (APPBadRequestException e) {
            throw e;
        }
        catch (APPNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new APPInternalServerException(0, e.getMessage());
        }
    }


    private Owner convertDocumentToOwner(Document item) {
        Owner owner = new Owner(
                item.getString("userName"),
                item.getString("firstName"),
                item.getString("lastName"),
                item.getString("prefGender"),
                item.getString("prefJob"),
                item.getInteger("prefNum"),
                item.getString("prefCook"),
                item.getString("emailAddress"),
                item.getString("password")
                );
        owner.setId(item.getObjectId("_id").toString());
        return owner;
    }



} // end of main()
