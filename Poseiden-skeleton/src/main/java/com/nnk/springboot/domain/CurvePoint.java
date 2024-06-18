package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    @Id
    @Column(name = "id")
    private int id;
    public CurvePoint(int i, double v, double v1) {

    }

    public Integer getId() {
        return null;
    }

    public int getCurveId() {
        return 0;
    }

    public void setCurveId(int i) {

    }
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields
}
