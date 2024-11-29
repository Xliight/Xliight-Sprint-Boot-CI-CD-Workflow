package com.ouitrips.app.services.messagingsystemservice.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @JsonProperty("groupId")
    private int groupId;
    @JsonProperty("groupName")
    private String groupName;
    private int id;
    private String name;
    private String description;
    private String action;
}