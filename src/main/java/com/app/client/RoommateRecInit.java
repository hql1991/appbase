package com.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class RoommateRecInit {

    public static void init(String[] renterIds, List<String>[] roommateIds) {
        doDeleteAll();
        doPost(renterIds[0], roommateIds[0]);
        doPost(renterIds[1], roommateIds[1]);

        doGetAll();
    }

    public static void doPost(String renterId, List<String> roommate_recolist){
        try {
            URL url = new URL("http://localhost:8080/api/rec/renters");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject renterRec = new JSONObject();
            renterRec.put("renterId",renterId);
            renterRec.put("roommate_recolist",roommate_recolist);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(renterRec.toString());
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
            URL url = new URL("http://localhost:8080/api/rec/renters");
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
            URL url = new URL("http://localhost:8080/api/rec/renters");
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

