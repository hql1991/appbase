package com.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Resource {

    public static void main(String[] argv) {
        doDeleteAll();
        doPost("Jim123", "Jim", "Madison","Male", "Student", 1, "No cook");
        doPost("Pineapple", "Lisa", "Cooper","Female", "Employed", 2, "Cook");
        doPost("TJTJ", "Tom", "Green","Male", "Employed", 1, "Cook");
        doGetAll();
    }

    public static void doPost(String userName, String firstName, String lastName, String prefGender, String prefJob, int prefNum, String prefCook){
        try {
            URL url = new URL("http://localhost:8080/api/owners");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);

            con.setDoOutput(true);

            JSONObject owner = new JSONObject();
            owner.put("userName",userName);
            owner.put("firstName",firstName);
            owner.put("lastName",lastName);
            owner.put("prefGender",prefGender);
            owner.put("prefJob",prefJob);
            owner.put("prefNum",prefNum);
            owner.put("prefCook",prefCook);

            OutputStreamWriter wr= new OutputStreamWriter(con.getOutputStream());
            wr.write(owner.toString());
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
            URL url = new URL("http://localhost:8080/api/owners");
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
            URL url = new URL("http://localhost:8080/api/owners");
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
