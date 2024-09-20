package net.tgburrin.timekeeping.controllers;

import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.InvalidRecordException;
import net.tgburrin.timekeeping.Tasks.TaskStartReq;
import net.tgburrin.timekeeping.Tasks.TaskStopReq;
import net.tgburrin.timekeeping.Tasks.TaskTime;
import net.tgburrin.timekeeping.services.TaskTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/task_time")
public class TaskTimeController {
    @Autowired
    TaskTimeService tsService;

    @PostMapping(value="/maintain/start_tasks/", consumes = "application/json", produces = "application/json")
    public List<TaskTime> startTasks(HttpSession s, @RequestBody TaskStartReq req) {
        return tsService.startTasks(req.userId, req.taskIds);
    }

    @PostMapping(value="/maintain/stop_tasks/", consumes = "application/json", produces = "application/json")
    public List<TaskTime> stopTasks(HttpSession s, @RequestBody TaskStopReq req) {
        return tsService.stopTasks(req.taskTimeIds);
    }

    @RequestMapping(value="/read/user/{id}", method= RequestMethod.GET)
    public List<TaskTime> findUserActiveTasks(HttpSession s, @PathVariable("id") Long id) {
        return tsService.findAllActiveUserTasks(id);
    }

    @RequestMapping(value="/read/id/{id}", method= RequestMethod.GET)
    public TaskTime findTaskTime(HttpSession s, @PathVariable("id") UUID id) throws Exception {
        TaskTime t = tsService.findTaskTimeById(id);
        if(t == null)
            throw new InvalidRecordException("Task time id "+id.toString()  +" could not be found");
        return t;
    }
}
