package com.app.client;

import com.app.server.services.RentalService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RentalInit {
    private static RentalService rentalService = RentalService.getInstance();

    public static String[] init(String[] renterIds, String[] ownerIds) {
        doDeleteAll();
        String[] rentalIds = new String[3];
        rentalIds[0] = doPost(renterIds[0], ownerIds[0], "08202018", "08192019", "08102018");
        rentalIds[1] = doPost(renterIds[1], ownerIds[1], "06102017", "08102018", "06012017");
        rentalIds[2] = doPost(renterIds[2], ownerIds[2], "01102013", "09102013", "01092013");
        doGetAll();
        return rentalIds;
    }

    public static String doPost(String renterId, String ownerId, String startDate, String endDate, String timeStamp) {

        JSONObject rental = new JSONObject();
        rental.put("renterId", renterId);
        rental.put("ownerId", ownerId);
        rental.put("startDate", startDate);
        rental.put("endDate", endDate);
        rental.put("timeStamp", timeStamp);

        return rentalService.create(rental.toMap()).getId();

    }


    public static void doGetAll() {
        try {
            URL url = new URL("http://localhost:8080/api/rentals");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doDeleteAll() {
        try {
            URL url = new URL("http://localhost:8080/api/rentals");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            int status = con.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println(content);
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

