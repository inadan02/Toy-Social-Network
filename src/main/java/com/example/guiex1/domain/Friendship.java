package com.example.guiex1.domain;


import java.time.LocalDateTime;


public class Friendship extends Entity<Tuple<Long,Long>>{
    LocalDateTime friendsFrom;
    public Friendship(){this.friendsFrom=LocalDateTime.now();}
    public Friendship(LocalDateTime localDateTime){this.friendsFrom=localDateTime;}

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public String toString() {
        return "Friendship{ID=" +
                this.getId()+
                "  friendsFrom=" + friendsFrom +
                '}';
    }
}
