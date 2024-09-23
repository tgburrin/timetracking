package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TaskTimeDeleteReq {
    @JsonProperty("task_time_id")
    public UUID taskTimeId;
}
