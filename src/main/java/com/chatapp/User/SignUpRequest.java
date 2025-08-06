package com.chatapp.User;

public class SignUpRequest {
    public String email;
    public String password;
    public boolean returnSecureToken = true;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}