package com.sah.taskmanager.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @Size(max = 50)
    @NotBlank
    private String userName;


    @Size(max = 255)
    @NotBlank
    @Email
    private String email;

    @NotBlank
   @Size(min = 8, max = 255)
    private String password;

    public RegisterRequest(){

    }

    public RegisterRequest(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
