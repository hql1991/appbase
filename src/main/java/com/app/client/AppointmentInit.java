package com.app.client;

import com.app.server.services.AppointmentService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppointmentInit {
    private static AppointmentService appointmentService = AppointmentService.getInstance();

    public static String[] init(String[] senderIds, String[] receiverIds) {
        doDeleteAll();
        String[] ids = new String[4];
        ids[0] = doPost("Hi can we meet sometime?",senderIds[0], receiverIds[0], "01/10/2018", "false");
        ids[1] = doPost("I'd like to arrange a meeting with you.",senderIds[1], receiverIds[1], "29/10/2018", "false");
        ids[2] = doPost("Let's meet Jackals!",senderIds[2], receiverIds[2], "12/11/2018", "true");
        doGetAll();
        return ids;
    }

    public static String doPost(String detail, String senderId, String receiverId, String meetTime, String accepted) {

        JSONObject appointment = new JSONObject();
        appointment.put("detail", detail);
        appointment.put("senderId", senderId);
        appointment.put("receiverId", receiverId);
        appointment.put("meetTime", meetTime);
        appointment.put("accepted", accepted);

        return appointmentService.create(appointment.toMap()).getId();
    }


    public static void doGetAll() {
        try {
            URL url = new URL("http://localhost:8080/api/appointments");
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
            URL url = new URL("http://localhost:8080/api/appointments");
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
