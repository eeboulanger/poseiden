package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @NotNull(message = "Must not be null")
    @Min(value = 1, message = "Enter a number higher than 0")
    private Byte curveId;

    @NotNull(message = "Enter a number")
    @Min(value = 1, message = "Enter a number higher than 0")
    private Double term;

    @NotNull(message = "Enter a number")
    @Min(value = 1, message = "Enter a number higher than 0")
    @Column(name = "`value`") //Reserved word
    private Double value;

    private Timestamp asOfDate;

    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Byte curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
