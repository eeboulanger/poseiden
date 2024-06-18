package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @Column(name = "id")
    private int id;
    public Rating(String moodysRating, String sandPRating, String fitchRating, int i) {

    }

    public Integer getId() {
        return null;
    }

    public int getOrderNumber() {
        return 0;
    }

    public void setOrderNumber(int i) {

    }
    // TODO: Map columns in data table RATING with corresponding java fields
}
