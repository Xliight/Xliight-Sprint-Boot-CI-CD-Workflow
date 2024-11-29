package com.ouitrips.app.services.messagingsystemservice.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataResponse {
    private String bulkId;
    private List<MessageData> messages;
}
