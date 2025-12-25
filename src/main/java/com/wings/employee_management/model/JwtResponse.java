package com.wings.employee_management.model;

public class JwtResponse {

    public JwtResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public JwtResponse(String token) {
        this.token = token;
    }
}
