package com.chatapp.User;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.chatapp.API.API;
import com.chatapp.service.FirestoreService;
import com.google.gson.Gson;

public class FirebaseLogin {

    
    private static FirebaseUser currentUser;

    public static String loginUser(String email, String password) {
        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API.API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInput = String.format("""
                    {
                      "email": "%s",
                      "password": "%s",
                      "returnSecureToken": true
                    }
                    """, email, password);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (connection.getResponseCode() == 200) {

                Scanner scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                Gson gson = new Gson();
                currentUser = gson.fromJson(response, FirebaseUser.class);
                System.out.println("Login successful");
                FirestoreService.createProfile(currentUser);
                return response;
            } else {
                Scanner scanner = new Scanner(connection.getErrorStream()).useDelimiter("\\A");
                String error = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
                System.err.println("Login failed: " + error);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

}
