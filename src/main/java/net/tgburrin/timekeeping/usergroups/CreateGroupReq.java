package net.tgburrin.timekeeping.usergroups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGroupReq {
    @JsonProperty("group_name")
    public String groupName;
}
