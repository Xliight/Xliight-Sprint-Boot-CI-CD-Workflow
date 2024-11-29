package com.ouitrips.app.services.messagingsystemservice.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataEmailProviders {
    private String host;
    private String username;
    private String password;
    private String from;
    private String fromName;
    private int port;
}