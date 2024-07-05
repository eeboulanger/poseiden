package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String name;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String description;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String json;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String template;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String sqlStr;

    @NotBlank(message = "")
    @NotEmpty(message = "Must not be empty")
    private String sqlPart;

    public RuleName() {
    }

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
