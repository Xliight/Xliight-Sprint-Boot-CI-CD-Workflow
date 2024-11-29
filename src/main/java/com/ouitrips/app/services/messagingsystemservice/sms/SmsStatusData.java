package com.ouitrips.app.services.messagingsystemservice.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsStatusData {
    private String bulkId;
    private String status;
}
