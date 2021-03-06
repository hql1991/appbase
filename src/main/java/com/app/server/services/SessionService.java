package com.app.server.services;

import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.Hash;
import com.app.server.models.Owner;
import com.app.server.models.Renter;
import com.app.server.models.Session;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import com.app.server.http.utils.APPCrypt;

import javax.ws.rs.core.HttpHeaders;

/**
 * Services run as singletons
 */

public class SessionService {

    private static SessionService self;
    private ObjectWriter ow;
    private MongoCollection<Document> ownerCollection = null;
    private MongoCollection<Document> renterCollection = null;

    private SessionService() {
        this.ownerCollection = MongoPool.getInstance().getCollection("owners");
        this.renterCollection = MongoPool.getInstance().getCollection("renters");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static SessionService getInstance() {
        if (self == null)
            self = new SessionService();
        return self;
    }

    public Session create(Object request) {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
            if (!json.has("email"))
                throw new APPBadRequestException(55, "missing email");
            if (!json.has("password"))
                throw new APPBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            query.put("email", json.getString("email"));
            //query.put("password", Hash.MD5Hash(json.getString("password")));
            //query.put("password", json.getString("password"));

            Document item = ownerCollection.find(query).first();
            if (item == null) {
                item = renterCollection.find(query).first();
                if (item == null)
                    throw new APPNotFoundException(0, "No user found matching credentials");

                Renter renter = RenterService.convertDocumentToRenter(item);
                String saltedHash = Hash.PBKDF2Hash(json.getString("password"), renter.getSalt());
                if (!saltedHash.equals(renter.getPassword()))
                    throw new APPNotFoundException(0, "No user found matching credentials");

                renter.setId(item.getObjectId("_id").toString());
                return new Session(renter);
            }

            Owner owner = OwnersService.convertDocumentToOwner(item);
            String saltedHash = Hash.PBKDF2Hash(json.getString("password"), owner.getSalt());
            if (!saltedHash.equals(owner.getPassword()))
                throw new APPNotFoundException(0, "No user found matching credentials");

            owner.setId(item.getObjectId("_id").toString());
            return new Session(owner);
        } catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        } catch (APPBadRequestException e) {
            throw e;
        } catch (APPNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new APPInternalServerException(0, e.getMessage());
        }
    }

    public static boolean validateToken(String userId, HttpHeaders httpHeaders) {

        String AUTHENTICATION_SCHEME = "Bearer";
        String authorizationString = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationString == null || !authorizationString.startsWith(AUTHENTICATION_SCHEME)) {
            throw new APPUnauthorizedException(99, "Invalid token!");
        }

        String token = authorizationString.substring(AUTHENTICATION_SCHEME.length()).trim();
        String decrypted;

        try {
            decrypted = APPCrypt.decrypt(token);
        } catch (Exception e) {
            throw new APPUnauthorizedException(99, "Invalid token!");
        }

        if (userId.equals(decrypted))
            return true;
        else
            throw new APPUnauthorizedException(100, "UnAuthorized user!");
    }

} // end of main()
