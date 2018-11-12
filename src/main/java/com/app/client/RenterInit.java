package com.app.client;

import com.app.server.models.Renter;
import com.app.server.services.RenterService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RenterInit {
    private static RenterService renterService = RenterService.getInstance();

    public static void main(String[] argv) {
        doDeleteAll();

        String[] ids = new String[3];

        ids[0] = doPost("Qianli", "Hu", "qianli.hu@sv.cmu.edu", 1, 1, 1, 1, 1, 1, 1, 0);
        ids[1] = doPost("James", "Harden", "james.harden@rockets.nba.com", 1, 2, 1, 0, 0, 3, 1, 1);
        ids[2] = doPost("Yuki", "Wang", "yuki.wang@sv.cmu.edu", 0, 1, 2, 2, 2, 2, 2, 2);

        PhotoInit.init(ids);

        doGetAll();
    }

    public static String doPost(String firstName, String lastName, String email, int gender, int job,
                                int num, int cook, int pref_gender, int pref_job, int pref_num, int pref_cook) {
//        try {
//            URL url = new URL("http://localhost:8080/api/renters");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
//            con.setDoInput(true);
//
//            con.setDoOutput(true);
//
//
//            JSONObject renter = new JSONObject();
//            renter.put("firstName", firstName);
//            renter.put("lastName", lastName);
//            renter.put("email", email);
//            renter.put("gender", gender);
//            renter.put("job", job);
//            renter.put("num", num);
//            renter.put("cook", cook);
//            renter.put("pref_gender", pref_gender);
//            renter.put("pref_job", pref_job);
//            renter.put("pref_num", pref_num);
//            renter.put("pref_cook", pref_cook);
//
//
//            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//            wr.write(renter.toString());
//            wr.flush();
//
//
//            int status = con.getResponseCode();
//            System.out.println(status);
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            System.out.println(content);
//            con.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        JSONObject renterJSON = new JSONObject();
        renterJSON.put("firstName", firstName);
        renterJSON.put("lastName", lastName);
        renterJSON.put("email", email);
        renterJSON.put("gender", gender);
        renterJSON.put("job", job);
        renterJSON.put("num", num);
        renterJSON.put("cook", cook);
        renterJSON.put("pref_gender", pref_gender);
        renterJSON.put("pref_job", pref_job);
        renterJSON.put("pref_num", pref_num);
        renterJSON.put("pref_cook", pref_cook);

        Renter renter = renterService.create(renterJSON.toMap());
        return renter.getId();


    }


    public static void doGetAll() {
        try {
            URL url = new URL("http://localhost:8080/api/renters");
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
            URL url = new URL("http://localhost:8080/api/renters");
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
