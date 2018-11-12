package com.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RentalInit {

    public static void init(String[] argv) {
        doDeleteAll();
        doPost("5bdf54785861e26d0cf9e3cc", "5bdf54785861e26d0cf9e3cd", "08202018","08192019", "08102018");
        doPost("5bdf54785861e26d0cf9e3cc", "5bdf54785861e26d0cf9e3cd", "06102017","08102018", "06012017");
        doGetAll();
    }

    public static void doPost(String renterId, String ownerId, String startDate, String endDate, String timeStamp){
        try {
            URL url = new URL("http://localhost:8080/api/rentals");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject rental = new JSONObject();
            rental.put("renterId",renterId);
            rental.put("ownerId",ownerId);
            rental.put("startDate",startDate);
            rental.put("endDate",endDate);
            rental.put("timeStamp",timeStamp);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(rental.toString());
            wr.flush();


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
        }
        catch(Exception e) {
            e.printStackTrace();

        }

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

