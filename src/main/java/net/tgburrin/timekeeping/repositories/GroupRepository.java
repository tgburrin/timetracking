package net.tgburrin.timekeeping.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import net.tgburrin.timekeeping.UserGroups.Group;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    @Query("select * from timekeeping.usergroups where type='G' and status = 'A'")
    List<Group> findAllActive();

    @Query("insert into timekeeping.usergroups (name,type,group_id) values (:name, 'G', :groupId)")
    Group addGroup(@Param("name") String name, @Param("groupId") Long groupId );
}
