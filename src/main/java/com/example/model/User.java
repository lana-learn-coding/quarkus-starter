package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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
