package net.tgburrin.timekeeping.Tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tgburrin.timekeeping.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

public class TaskStartReq {
    @JsonProperty("user_id")
    public Long userId;
    @JsonProperty("task_ids")
    public List<Long> taskIds;

    public void validate() {
        ArrayList<String> msgs = new ArrayList<>();
        if ( userId == null || userId <= 0 )
            msgs.add("User Id must be >= 0");
        if ( taskIds == null || taskIds.size() == 0 )
            msgs.add("At least one task id must be provide");
        if ( !msgs.isEmpty() )
            throw new InvalidDataException(String.join("\n", msgs));
    }
}
