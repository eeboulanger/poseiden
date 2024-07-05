package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String moodysRating;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String sandPRating;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String fitchRating;

    @NotNull(message = "Enter a number")
    @Min(value = 1, message = "Enter a number higher than 0")
    private Integer orderNumber;

    public Rating() {
    }

    public Rating(String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
