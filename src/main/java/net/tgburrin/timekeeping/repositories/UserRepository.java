package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.UserGroups.Group;
import net.tgburrin.timekeeping.UserGroups.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>  {
    @Override
    @Query("select * from timekeeping.users where user_id=:id")
    Optional<User> findById(@Param("id") Long userId);

    @Query("select * from timekeeping.users where name=:name")
    Optional<User> findByName(@Param("name") String userName);

    @Query("select * from timekeeping.users order by updated desc")
    List<User> listUsers();

    @Query("select * from timekeeping.users where group_id = :gid order by name")
    List<User> listUsersInGroup(@Param("gid") Long groupId);
}
