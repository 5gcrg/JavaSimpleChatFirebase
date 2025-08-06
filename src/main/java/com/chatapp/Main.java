package com.chatapp;

import java.util.Scanner;

import com.chatapp.User.CreateUser;
import com.chatapp.User.FirebaseLogin;
import com.chatapp.User.User;
import com.chatapp.service.FirestoreService;

public class Main {

    private static boolean isLoggedIn = true;

    public static void checkLogin() {

        if (isLoggedIn) {
            new Thread(() -> {
                login();
            }).start();

        } else {
            new Thread(() -> {
                register();
            }).start();

        }

    }

    public static void main(String[] args) {

        System.out.println("Welcome to my Chat App!");
        checkLogin();

    }

    public static void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Create Account");
        System.out.println();
        while (true) {

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            if (!email.contains("@") || password.length() < 8) {
                System.out.println("Invalid email format! or Password too short!");
                continue;
            }

            User user = new User(email, password);

            try {

                CreateUser.createAccount(user.getEmail(), user.getPassword());

                login();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public static void login() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Login");
        System.out.println();

        while (true) {

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            if (!email.contains("@") || password.length() < 8) {
                System.out.println("Invalid email format! or Password too short!");
                continue;
            }

            User user = new User(email, password);

            try {
                FirebaseLogin.loginUser(user.getEmail(), user.getPassword());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }
}