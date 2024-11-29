package com.ouitrips.app.services.messagingsystemservice.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageStatutData {
    private String description;
    private Integer groupId;
    private String groupName;
    private Integer id;
    private String name;
}
