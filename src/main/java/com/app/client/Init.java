package com.app.client;

import com.app.server.models.Renter;
import com.app.server.services.RenterService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Init {
    private static RenterService renterService = RenterService.getInstance();

    public static void main(String[] argv) {

        String[] renterIds = RenterInit.init();
        PhotoInit.init(renterIds);
        String[] ownerIds = OwnerInit.init();
        HouseInit.init(ownerIds);
        RentalInit.init(renterIds, ownerIds);

    }


}
