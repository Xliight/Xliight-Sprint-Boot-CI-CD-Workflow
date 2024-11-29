package com.ouitrips.app.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignInRequest(@JsonProperty("login") String username, String password) {
    public SignInRequest withUsername(String username){
        return new SignInRequest(username, password());
    }


}