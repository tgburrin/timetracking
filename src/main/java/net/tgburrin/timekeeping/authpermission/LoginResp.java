package net.tgburrin.timekeeping.authpermission;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tgburrin.timekeeping.usergroups.User;

public class LoginResp {
    public User user;
    @JsonProperty("login_success")
    public Boolean loginSuccess;

    @JsonProperty("session_id")
    public String sessionId;
}
