package net.tgburrin.timekeeping.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tgburrin.timekeeping.exceptions.InternalErrorException;
import net.tgburrin.timekeeping.exceptions.InvalidDataException;
import net.tgburrin.timekeeping.tasks.Task;
import net.tgburrin.timekeeping.tasks.TaskStartReq;
import net.tgburrin.timekeeping.tasks.TaskStopReq;
import net.tgburrin.timekeeping.tasks.TaskTime;
import net.tgburrin.timekeeping.repositories.TaskRepository;
import net.tgburrin.timekeeping.repositories.TaskTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskTimeRepository tskTimeRepo;

    @Autowired
    TaskRepository tskRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //@Transactional
    public List<TaskTime> startTasks(Long userId, List<Long> taskIds) {
        return tskTimeRepo.startTasks(userId, taskIds);
    }

    public List<TaskTime> startTasks(TaskStartReq req) {
        return tskTimeRepo.startTasks(req.userId, req.taskIds);
    }

    //@Transactional
    public List<TaskTime> stopTasks(Long userId, List<Long> taskIds) {
        return tskTimeRepo.stopTasks(userId, taskIds);
    }

    public List<TaskTime> stopTasks(TaskStopReq req) {
        return tskTimeRepo.stopTasks(req.userId, req.taskIds);
    }

    public TaskTime createTaskTime(TaskTime newTaskTime) {
        try {
            return tskTimeRepo.maintain(newTaskTime);
        } catch (SQLDataException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    public TaskTime updateTaskTime(UUID taskTimeId, Instant newStartDt, Instant newEndDt) {
        Long cnt;
        if ( !newStartDt.isBefore(newEndDt))
            throw new InvalidDataException("Start datetime is after or equal to the end datetime");
        if ( (cnt = tskTimeRepo.checkConflictingRange(taskTimeId, newStartDt, newEndDt)) > 0 )
            throw new InvalidDataException("Cannot update time range with "+cnt+" conflicting records for the same user");
        return tskTimeRepo.setTaskTimes(taskTimeId, newStartDt, newEndDt);
    }

    public TaskTime updateTaskTime(TaskTime tt) {
        tt.validate();
        try {
            return tskTimeRepo.maintain(tt);
        } catch (SQLDataException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    public TaskTime deleteTaskTime(UUID taskTimeId) {
        if ( !tskTimeRepo.testTaskTimeIdExists(taskTimeId) )
            throw new InvalidDataException("Task time id "+taskTimeId+" could not be located");
        return tskTimeRepo.deleteTaskTime(taskTimeId);
    }

    public List<TaskTime> findAllActiveUserTasks(Long userId) {
        return tskTimeRepo.findAllActiveUserTasks(userId);
    }

    public List<TaskTime> findAllActiveTasks() {
        return tskTimeRepo.findAllActiveTasks();
    }

    public TaskTime findTaskTimeById(UUID id) {
        return tskTimeRepo.findById(id).orElse(null);
    }

    // --- Task Methods
    public List<Task> findOpenTasks() {
        return tskRepo.findOpenTasks();
    }

    public Task createTask(Task newTask) throws JsonProcessingException, SQLDataException {
        return tskRepo.maintain(newTask);
    }
}
