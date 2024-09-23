package net.tgburrin.timekeeping.controllers;

import jakarta.servlet.http.HttpSession;
import net.tgburrin.timekeeping.exceptions.InternalErrorException;
import net.tgburrin.timekeeping.exceptions.InvalidDataException;
import net.tgburrin.timekeeping.exceptions.InvalidRecordException;
import net.tgburrin.timekeeping.tasks.*;
import net.tgburrin.timekeeping.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLDataException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/${apiPrefix}/${apiVersion}/task_time")
public class TaskTimeController {
    @Autowired
    TaskService tsService;

    @PostMapping(value="/maintain/start_tasks/", consumes = "application/json", produces = "application/json")
    public List<TaskTime> startTasks(HttpSession s, @RequestBody TaskStartReq req) {
        return tsService.startTasks(req.userId, req.taskIds);
    }

    @PostMapping(value="/maintain/stop_tasks/", consumes = "application/json", produces = "application/json")
    public List<TaskTime> stopTasks(HttpSession s, @RequestBody TaskStopReq req) {
        return tsService.stopTasks(req.userId, req.taskIds);
    }

    @PostMapping(value = "/maintain/task_time/", consumes = "application/json", produces = "application/json")
    public TaskTime createTaskTime (@RequestBody TaskTimeCreateReq req) {
        if ( req.startDt == null || req.endDt == null )
            throw new InvalidDataException("Task time start/end may not be null");
        if ( !req.startDt.isBefore(req.endDt) )
            throw new InvalidDataException("Task time start must be before end");

        return tsService.createTaskTime(new TaskTime(req));
    }
    @PutMapping(value = "/maintain/task_time/", consumes = "application/json", produces = "application/json")
    public TaskTime updateTaskTime (@RequestBody TaskTimeUpdateReq req) {
        TaskTime ut = tsService.findTaskTimeById(req.taskTimeId);
        ut.setStartDt(req.startDt);
        ut.setEndDt(req.endDt);
        return tsService.updateTaskTime(ut);
    }

    @DeleteMapping(value = "/maintain/task_time/", consumes = "application/json", produces = "application/json")
    public TaskTime deleteTaskTime (@RequestBody TaskTimeDeleteReq req) {
        TaskTime rv = tsService.deleteTaskTime(req.taskTimeId);
        if ( rv == null )
            throw new InvalidRecordException("Task time id "+req.taskTimeId+" could not be found");
        return rv;
    }

    @RequestMapping(value="/read/id/{id}", method= RequestMethod.GET)
    public TaskTime findTaskTime(HttpSession s, @PathVariable("id") UUID id) {
        TaskTime t = tsService.findTaskTimeById(id);
        if(t == null)
            throw new InvalidRecordException("Task time id "+id.toString()  +" could not be found");
        return t;
    }

    @RequestMapping(value="/list/by_user/{id}", method= RequestMethod.GET)
    public List<TaskTime> findUserActiveTasks(HttpSession s, @PathVariable("id") Long id) {
        return tsService.findAllActiveUserTasks(id);
    }

    @RequestMapping(value="/list/tasks/active", method= RequestMethod.GET)
    public List<TaskTime> findActiveTasks(HttpSession s, @RequestParam(value = "user_id", required = false) Long userId) throws Exception {
        return userId == null ? tsService.findAllActiveTasks() : tsService.findAllActiveUserTasks(userId);
    }
}
