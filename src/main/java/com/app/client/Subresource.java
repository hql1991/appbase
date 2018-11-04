package com.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Subresource {

    public static void main(String[] argv) {
        doDeleteAll("5bdf54785861e26d0cf9e3cc");
        doPost("5bdf54785861e26d0cf9e3cc", "70 Julia Cr", "CA","94043", "1990");
        doPost("5bdf54785861e26d0cf9e3cc", "1122 Lakeside Drive", "CA","94555", "2010");
        doPost("5bdf54785861e26d0cf9e3cc", "8 Mary Street", "CA","95035", "2000");
        doPost("5bdf54785861e26d0cf9e3cc", "123 Sky Way", "CA","94070", "2009");
        doGetAll("5bdf54785861e26d0cf9e3cc");
    }

    public static void doPost(String ownerid, String address, String state, String zipcode, String year){
        try {
            URL url = new URL("http://localhost:8080/api/owners/" + ownerid + "/houses");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject house = new JSONObject();
            house.put("ownerid",ownerid);
            house.put("address",address);
            house.put("state",state);
            house.put("zipcode",zipcode);
            house.put("year",year);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(house.toString());
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


    public static void doGetAll(String ownerid) {
        try {
            URL url = new URL("http://localhost:8080/api/owners/" + ownerid + "/houses");
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
            URL url = new URL("http://localhost:8080/api/owners/" + ownerid + "/houses");
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
