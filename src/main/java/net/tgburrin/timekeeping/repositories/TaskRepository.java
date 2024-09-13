package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.Tasks.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>  {
}
