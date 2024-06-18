package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
public class BidList {
    @Id
    @Column(name = "id")
    private int id;
    public BidList(String accountTest, String typeTest, double v) {

    }

    public Integer getBidListId() {
        return null;
    }

    public double getBidQuantity() {
        return 0;
    }

    public void setBidQuantity(double v) {

    }
    // TODO: Map columns in data table BIDLIST with corresponding java fields
}
