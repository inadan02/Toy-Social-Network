package com.example.guiex1.domain;

import com.example.guiex1.repository.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;
    String type;

    public Prietenie(LocalDateTime date) {
        date=date.truncatedTo(ChronoUnit.MINUTES);
        this.date = date;
        this.type="pending";
    }
    public Prietenie(LocalDateTime date,String type) {
        date=date.truncatedTo(ChronoUnit.MINUTES);
        this.date = date;
        this.type=type;
    }
    public Prietenie() {}

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "Id1: " + super.getId().getLeft() +
                ", Id2: " + super.getId().getRight() +
                ", date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                '}';
    }
    public Long getId1(){return super.getId().getLeft();}
    public Long getId2(){return super.getId().getRight();}
//    public String getName1(){
//        return
//    }
    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }
}
