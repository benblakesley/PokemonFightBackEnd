package com.blakesley.pokemonfightback.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {

    public User() {
    }

    public User(String username, Integer score, String password) {
        this.username =username;
        this.score = score;
        this.password = password;
    }

    @Id
    @Column(name="username")
    private String username;

    @Column(name="score")
    private Integer score;

    @Column(name="password")
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", score=" + score +
                '}';
    }
}
