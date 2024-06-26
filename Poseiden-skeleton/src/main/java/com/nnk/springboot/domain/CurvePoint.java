package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@Table(name = "CurvePoint")
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotNull(message = "must not be null")
    private Byte curveId;
    private Timestamp asOfDate;
    @NotNull
    private Double term;
    @NotNull
    @Column(name = "`value`") //Reserved word
    private Double value;
    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Byte curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
