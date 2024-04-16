package com.example.ingradtransport.model;

public class AuthCheckRequest {
    public AuthCheckRequest(String token, int user_id) {
        this.token = token;
        this.user_id = user_id;
    }

    private String token;
    private int user_id;
}
