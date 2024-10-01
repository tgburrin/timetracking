package net.tgburrin.timekeeping.repositories;

import net.tgburrin.timekeeping.usergroups.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, SharedRepositoryInt<User, Long>  {
    @Query("select count(*) = 1 as e from timekeeping.users where user_id=:id")
    Boolean testIdExists(@Param("id") Long userId);

    @Query("select * from timekeeping.users where name=:name")
    Optional<User> findByName(@Param("name") String userName);

    @Query("select count(*) = 1 as e from timekeeping.users where name=:name")
    Boolean testNameExists(@Param("name") String username);

    @Query("select * from timekeeping.users order by updated desc")
    List<User> listUsers();

    @Query("select * from timekeeping.users where group_id = :gid order by name")
    List<User> listUsersInGroup(@Param("gid") Long groupId);
}
