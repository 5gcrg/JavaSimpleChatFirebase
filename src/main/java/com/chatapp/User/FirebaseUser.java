package com.chatapp.User;


public class FirebaseUser {
    public String idToken;
    public String email;
    public String refreshToken;
    public String expiresIn;
    public String localId;

    @Override
    public String toString() {
        return "FirebaseUser{" +
                "email='" + email + '\'' +
                ", idToken='" + idToken + '\'' +
                ", localId='" + localId + '\'' +
                '}';
    }
}

