package com.ouitrips.app.services.messagingsystemservice.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataApiProviderSms {
    private String apiKey;
    private String baseUrl;
}
