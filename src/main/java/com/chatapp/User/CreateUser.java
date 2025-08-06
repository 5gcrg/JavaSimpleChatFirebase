package com.chatapp.User;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.chatapp.API.API;
import com.google.gson.Gson;

public class CreateUser {

  

    public static SignUpResponse createAccount(String email, String password) {

        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API.API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);

            SignUpRequest request = new SignUpRequest(email, password);

            Gson gson = new Gson();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = gson.toJson(request).getBytes();
                os.write(input);
            }
            int barWidth = 10;
            for (int i = 0; i <= barWidth; i++) {
                StringBuilder bar = new StringBuilder();
                bar.append("\rSigning Up: [");
                for (int j = 0; j < i; j++) {
                    bar.append('=');
                }
                for (int j = i; j < barWidth; j++) {
                    bar.append(' ');
                }
                bar.append("]");
                System.out.print(bar);
                try {
                    Thread.sleep(200); // Pause for animation effect
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println();

            InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream()
                    : connection.getErrorStream();

            try {
                Reader reader = new InputStreamReader(inputStream);

                return gson.fromJson(reader, SignUpResponse.class);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
