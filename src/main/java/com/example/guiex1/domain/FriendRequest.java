package com.example.guiex1.domain;

import java.time.LocalDateTime;

public class FriendRequest{
    public String firstName,lastName,type;
    public LocalDateTime date;
    public FriendRequest(String firstName,String lastName,String type,LocalDateTime date){
        this.firstName=firstName;
        this.lastName=lastName;
        this.type=type;
        this.date=date;
    }

    public String getFirstNameFr() {
        return firstName;
    }

    public String getLastNameFr() {
        return lastName;
    }

    public String getTypeFriendship() {
        return type;
    }
    public LocalDateTime getDate() {
        return date;
    }
}
