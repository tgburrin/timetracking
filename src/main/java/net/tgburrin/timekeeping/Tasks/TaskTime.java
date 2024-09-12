package net.tgburrin.timekeeping.Tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "task_time_instances")
public class TaskTime {
    @Id
    private UUID id;
    @Column("user_id")
    @JsonProperty("user_id")
    private Long userId;
    @Column("task_id")
    @JsonProperty("task_id")
    private Long taskId;

    @Column("start_dt")
    private Instant startDt;
    @Column("end_dt")
    private Instant endDt;


    public TaskTime() {
        this.id = UUID.randomUUID();
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
        s.add("Id: "+this.id.toString());
        s.add("userId: "+this.userId);
        s.add("taskId: "+this.taskId);
        s.add("lowerTime: "+this.startDt.toString());
        s.add("upperTime: "+(this.endDt == null ? "" : this.endDt.toString()));

        return String.join("\n", s);
    }
}
