package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;


@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @Column(name = "id")
    private int id;
    public Trade(String tradeAccount, String type) {

    }

    public Integer getTradeId() {
        return null;
    }

    public Object getAccount() {
        return null;
    }

    public void setAccount(String tradeAccountUpdate) {

    }
    // TODO: Map columns in data table TRADE with corresponding java fields
}
