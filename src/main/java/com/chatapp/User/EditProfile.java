package com.chatapp.User;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EditProfile {

    private static final String PROJECT_ID = "chatapp-47909";
    private static final String COLLECTION_USERS = "users";
    private static final String FIRESTORE_URL_PROFILE = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID +
            "/databases/(default)/documents/" + COLLECTION_USERS;

    public static void EditProfile(FirebaseUser firebaseUser) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();

        String uid = UUID.randomUUID().toString();

        String dateUpdated = LocalDate.now().toString();

        CreateProfile createProfile = new CreateProfile(username, uid, dateUpdated);
        try {

            URL url = new URL(FIRESTORE_URL_PROFILE + "/" + firebaseUser.email);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setRequestProperty("Authorization", "Bearer " + firebaseUser.idToken);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            JsonObject fields = new JsonObject();

            JsonObject usernameField = new JsonObject();
            usernameField.add("stringValue", new JsonPrimitive(createProfile.getUsername()));
            fields.add("username", usernameField);

            JsonObject idField = new JsonObject();
            idField.add("stringValue", new JsonPrimitive(createProfile.getUUID()));
            fields.add("uuid", idField);

            JsonObject timeField = new JsonObject();
            timeField.add("stringValue", new JsonPrimitive(createProfile.getDateUpdated()));
            fields.add("timeUpdated", timeField);

            JsonObject document = new JsonObject();
            document.add("fields", fields);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = document.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
                scanner.close();
            } else {
                Scanner scanner = new Scanner(connection.getErrorStream());
                while (scanner.hasNext()) {
                    System.err.println(scanner.nextLine());
                }
                scanner.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
      
    }

}
