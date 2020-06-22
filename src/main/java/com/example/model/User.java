package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class User {
    private String id;

    private String name;

    private String about;

    private Integer age;

    private String birth;

    private Integer height;

    private String email;

    private String password;

    private String phone;

    private String address;

    private Boolean activated;

    private Date activatedDate;

    private Date expirationDate;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
