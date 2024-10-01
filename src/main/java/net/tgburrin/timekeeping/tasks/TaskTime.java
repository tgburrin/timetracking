package net.tgburrin.timekeeping.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tgburrin.timekeeping.exceptions.InvalidDataException;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "task_time_instances")
public class TaskTime {
    private Instant created;
    private Instant updated;

    @Id
    @JsonProperty("task_time_id")
    private UUID id;
    @Column("user_id")
    @JsonProperty("user_id")
    private Long userId;
    @Column("task_id")
    @JsonProperty("task_id")
    private Long taskId;

    @Column("start_dt")
    @JsonProperty("start_dt")
    private Instant startDt;
    @Column("end_dt")
    @JsonProperty("end_dt")
    private Instant endDt;


    public TaskTime() {}

    public TaskTime(TaskTimeCreateReq newTaskTime) {
        this.userId = newTaskTime.userId;
        this.taskId = newTaskTime.taskId;
        this.startDt = newTaskTime.startDt;
        this.endDt = newTaskTime.endDt;
    }

    public Instant getCreated() {
        return created;
    }
    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Instant getStartDt() {
        return startDt;
    }

    public void setStartDt(Instant startDt) {
        this.startDt = startDt;
    }

    public Instant getEndDt() {
        return endDt;
    }

    public void setEndDt(Instant endDt) {
        this.endDt = endDt;
    }

    @Override
    public String toString() {
        List<String> s = new ArrayList<String>();
        s.add("Id: "+(this.id == null ? "" : this.id.toString()));
        s.add("userId: "+this.userId);
        s.add("taskId: "+this.taskId);
        s.add("lowerTime: "+this.startDt.toString());
        s.add("upperTime: "+(this.endDt == null ? "" : this.endDt.toString()));

        return String.join("\n", s);
    }

    public void validate() {
        if ( this.userId == null )
            throw new InvalidDataException("A user id is required");
        if ( this.taskId == null )
            throw new InvalidDataException("A task id is required");
        if ( this.startDt != null && this.endDt != null && !this.startDt.isBefore(this.endDt))
            throw new InvalidDataException("Start datetime must be before end datetime if provided");
    }
}
