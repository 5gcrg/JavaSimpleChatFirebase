package com.chatapp.service;

import com.chatapp.User.EditProfile;
import com.chatapp.User.FirebaseUser;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FirestoreService {

    private static final String PROJECT_ID = "chatapp-47909";
    private static final String COLLECTION_MESSAGES = "messages";
    private static final String COLLECTION_USERS = "users";
    private static final String FIRESTORE_URL = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID +
            "/databases/(default)/documents/" + COLLECTION_MESSAGES;

    private static final String FIRESTORE_URL_PROFILE = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID +
            "/databases/(default)/documents/" + COLLECTION_USERS;

    public static void sendMessage(String token, String messageText) {
        try {
            URL url = new URL(FIRESTORE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Create the message document
            JsonObject fields = new JsonObject();
            JsonObject messageField = new JsonObject();
            messageField.add("stringValue", new JsonPrimitive(messageText));
            fields.add("text", messageField);

            JsonObject document = new JsonObject();
            document.add("fields", fields);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = document.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
                scanner.close();
            } else {
                Scanner scanner = new Scanner(conn.getErrorStream());
                while (scanner.hasNext()) {
                    System.err.println(scanner.nextLine());
                }
                scanner.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void createProfile(FirebaseUser firebaseUser) {

        try {

            String uid = firebaseUser.email; // from signInWithPassword response
            URL url = new URL(FIRESTORE_URL_PROFILE + "/" + uid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setRequestProperty("Authorization", "Bearer " + firebaseUser.idToken);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            JsonObject fields = new JsonObject();
            JsonObject emailField = new JsonObject();
            emailField.add("stringValue", new JsonPrimitive(firebaseUser.email));
            fields.add("email", emailField);
            
            JsonObject document = new JsonObject();
            document.add("fields", fields);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = document.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Logged In!");
            } else {
                Scanner scanner = new Scanner(connection.getErrorStream());
                while (scanner.hasNext()) {
                    System.err.println(scanner.nextLine());
                }
                scanner.close();
            }

            
            EditProfile.EditProfile(firebaseUser);
         
           

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}