package net.tgburrin.timekeeping.UserGroups;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginSessionResp {
    @JsonProperty("session_id")
    private String sessionId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_name")
    private String userName;

    public LoginSessionResp(String s, Long ui, String un) {
        sessionId = s;
        userId = ui;
        userName = un;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
