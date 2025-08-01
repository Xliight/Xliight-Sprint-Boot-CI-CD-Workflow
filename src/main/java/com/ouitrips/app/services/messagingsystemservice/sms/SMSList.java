package com.ouitrips.app.services.messagingsystemservice.sms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSList {
    private List<SmsLogs> results;
}