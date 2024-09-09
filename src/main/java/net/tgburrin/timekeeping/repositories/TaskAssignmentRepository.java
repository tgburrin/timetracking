package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.Tasks.TaskAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends CrudRepository<TaskAssignment, Long> {
}
