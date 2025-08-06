package com.chatapp.service;
import com.chatapp.API.API;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RealtimeDatabaseService {

    
    private static final Gson gson = new Gson();

    public static void saveUserProfile(String uid, String idToken, String name, String email) {
        try {
            // Construct path: /users/uid.json?auth=ID_TOKEN
            URL url = new URL(API.DATABASE_URL + "users/" + uid + ".json?auth=" + idToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Construct JSON payload
            JsonObject user = new JsonObject();
            user.addProperty("name", name);
            user.addProperty("email", email);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(user).getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            System.out.println("Response Code: " + conn.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
