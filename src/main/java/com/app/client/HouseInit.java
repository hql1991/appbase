package com.app.client;

import com.app.server.services.HouseService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HouseInit {
    private static HouseService houseService = HouseService.getInstance();

    public static String[] init(String[] argv) {
        doDeleteAll("5bdf54785861e26d0cf9e3cc");
        String[] ids = new String[4];
        ids[0] = doPost(argv[0], "70 Julia Cr", "CA", "94043", "1990");
        ids[1] = doPost(argv[1], "1122 Lakeside Drive", "CA", "94555", "2010");
        ids[2] = doPost(argv[2], "8 Mary Street", "CA", "95035", "2000");
        ids[3] = doPost(argv[3], "123 Sky Way", "CA", "94070", "2009");
        doGetAll("5bdf54785861e26d0cf9e3cc");
        return ids;
    }

    public static String doPost(String ownerid, String address, String state, String zipcode, String year) {


        JSONObject house = new JSONObject();
        house.put("ownerid", ownerid);
        house.put("address", address);
        house.put("state", state);
        house.put("zipcode", zipcode);
        house.put("year", year);

        return houseService.create(house.toMap()).getId();

    }


    public static void doGetAll(String ownerid) {
        try {
            URL url = new URL("http://localhost:8080/api/houses");
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

    public static void doDeleteAll(String ownerid) {
        try {
            URL url = new URL("http://localhost:8080/api/houses");
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
