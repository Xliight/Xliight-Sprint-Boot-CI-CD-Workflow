package com.ouitrips.app.services.messagingsystemservice.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {
    private String response;
    private int httpCode;
    private String responseBody;
    private String error;
}
