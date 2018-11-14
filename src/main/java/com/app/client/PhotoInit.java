package com.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PhotoInit {
    public static void init(String[] argv) {
        doDeleteAll();
        doPost("/storage/photos/1.jpg", "28/04/1991", "28/04/1996", argv[0] );
        doPost("/storage/photos/2.jpg", "13/09/2016", "04/10/2016", argv[1]);
        doPost("/storage/photos/3.jpg", "09/10/2018", "09/11/2018", argv[2]);
        doGetAll();
    }

    public static void doPost(String url, String createDate, String editDate, String userId) {
        try {
            URL dbURL = new URL("http://localhost:8080/api/photos");
            HttpURLConnection con = (HttpURLConnection) dbURL.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject driver = new JSONObject();
            driver.put("url", url);
            driver.put("createDate", createDate);
            driver.put("editDate", editDate);
            driver.put("userId", userId);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(driver.toString());
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
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public static void doGetAll() {
        try {
            URL url = new URL("http://localhost:8080/api/photos");
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
            URL url = new URL("http://localhost:8080/api/photos");
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
