package net.tgburrin.timekeeping.UserGroups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserReq {
    public String name;
    @JsonProperty("group_name")
    public String groupName;
}
