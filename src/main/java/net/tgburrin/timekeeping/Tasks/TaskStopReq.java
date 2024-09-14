package net.tgburrin.timekeeping.Tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class TaskStopReq {
    @JsonProperty("task_time_ids")
    public List<UUID> taskTimeIds;
}
