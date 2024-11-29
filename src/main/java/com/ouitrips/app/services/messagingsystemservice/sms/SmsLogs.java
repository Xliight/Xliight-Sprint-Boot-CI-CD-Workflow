package com.ouitrips.app.services.messagingsystemservice.sms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsLogs {
    @JsonProperty("bulkId")
    private String bulkId;
    @JsonProperty("messageId")
    private String messageId;
    private String to;
    private String from;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonProperty("sentAt")
    private Date sentAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonProperty("doneAt")
    private Date doneAt;
    @JsonProperty("smsCount")
    private int smsCount;
    private String mccMnc;
    private Price price;
    private Status status;
}
