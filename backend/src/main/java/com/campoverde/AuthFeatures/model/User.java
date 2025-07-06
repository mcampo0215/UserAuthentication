package com.campoverde.AuthFeatures.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") //table name for users in PostgreSQL
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-incrementing ID
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    //used for email verification
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean verified;

    private String verificationToken;

    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
