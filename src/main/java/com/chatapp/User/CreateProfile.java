package com.chatapp.User;

import java.time.LocalDate;

public class CreateProfile {

    private String username;
    private String UUID;
    private String dateUpdated;

    public CreateProfile(String username, String UUID, String dateUpdated){
        this.username = username;
        this.UUID = UUID;
        this.dateUpdated = LocalDate.now().toString();
    }

    public String getUsername() {
        return username;
    }

    public String getUUID() {
        return UUID;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    


    

}
