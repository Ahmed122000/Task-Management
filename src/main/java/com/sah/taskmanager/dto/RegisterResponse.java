package com.sah.taskmanager.dto;

import java.time.Instant;

public class RegisterResponse {

    private Long id;
    private String userName;
    private String email;
    private Instant createdAt;

    public RegisterResponse(Long id, String userName, String email, Instant createdAt){
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

