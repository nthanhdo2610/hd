package com.tinhvan.hd.sms.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "groupId",
        "groupName",
        "id",
        "name",
        "description"
})
public class SMSLogsStatus {
    @JsonProperty("groupId")
    private float groupId;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("id")
    private float id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public float getGroupId() {
        return groupId;
    }

    public void setGroupId(float groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SMSLogsStatus{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}