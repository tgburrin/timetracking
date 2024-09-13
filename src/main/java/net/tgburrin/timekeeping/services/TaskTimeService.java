package net.tgburrin.timekeeping.services;

import net.tgburrin.timekeeping.Tasks.TaskTime;
import net.tgburrin.timekeeping.repositories.TaskTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TaskTimeService {
    @Autowired
    TaskTimeRepository tskTimeRepo;

    public TaskTime findTaskTimeById(UUID id) {
        Optional<TaskTime> ft = tskTimeRepo.findById(id);
        return ft.orElse(null);
    }

}
