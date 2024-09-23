package net.tgburrin.timekeeping.repositories;

import java.util.List;

import net.tgburrin.timekeeping.Tasks.Task;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>  {
    @Query("select * from timekeeping.tasks t where t.status = 'O' order by created")
    List<Task> findOpenTasks();

    /*
    @Query("insert into timekeeping.tasks " +
            "(status, external_id, external_status, external_description, external_details) " +
            "values " +
            "('O', :ext_id, :ext_status, :ext_desc, :ext_det)")
    Task createTask(
            @Param("ext_id") String externalId,
            @Param("ext_status") String externalStatus,
            @Param("ext_desc") String externalDescription,
            @Param("ext_det") String externalDetails
    );
     */
}
