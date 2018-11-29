package com.app.client;

import com.app.server.services.PaymentService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PaymentInit {
    private static PaymentService paymentService = PaymentService.getInstance();

    public static void init(String[] argv) {
        doDeleteAll();
        doPost("", "500", argv[0]);
        doPost("", "1000", argv[1]);
        doPost("", "233", argv[2]);
        doGetAll();
    }

    public static String doPost(String transactionId, String amount, String rentalId) {


        JSONObject payment = new JSONObject();
        payment.put("transactionId", transactionId);
        payment.put("amount", amount);
        payment.put("rentalId", rentalId);


        return paymentService.create(payment.toMap()).getId();

    }


    public static void doGetAll() {
        try {
            URL url = new URL("http://localhost:8080/api/payments");
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
            URL url = new URL("http://localhost:8080/api/payments");
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
