package net.tgburrin.timekeeping.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import net.tgburrin.timekeeping.UserGroups.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    @Query("select * from timekeeping.usergroups where type='G' and status = 'A'")
    List<Group> findAllActive();
}
