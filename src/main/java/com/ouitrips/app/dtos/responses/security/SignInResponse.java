package com.ouitrips.app.dtos.responses.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@lombok.Data
public class SignInResponse {
    private String status;
    private Data data;
    @lombok.Data
    public class Data {
        private Info info;
        @JsonProperty("data_user")
        private DataUser dataUser;

        @lombok.Data
        public class DataUser {
            private String avatar;
            private List<String> roles = new ArrayList<>();
            private String token = null;
            private String firstName = null;
            private String lastName = null;
            private String username;
            private String email;
            private String reference = null;
        }

        @lombok.Data
        public class Info {
            private boolean errorPassword;
            private boolean active;
            private boolean exist;
            private Integer lifetime;
            private boolean connected;
            private boolean locked;
        }
    }
}