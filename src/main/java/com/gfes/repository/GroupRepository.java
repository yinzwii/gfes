package com.gfes.repository;

import com.gfes.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String>, JpaSpecificationExecutor<Group> {

    int countByName(String name);

    @Query(value = "SELECT id from `group` WHERE name = ?1",nativeQuery = true)
    String queryGroupByName(String groupName);

    @Query("select distinct u.name from User u join UserGroup ug on u.id = ug.userId where ug.groupId = ?1 ")
    List<String> queryMembers(String groupId);

    @Query("select distinct u.name from User u where " +
            "(select count(ug) from UserGroup ug where ug.userId = u.id and ug.groupId = ?1 ) = 0  ")
    List<String> queryNonmembers(String groupId);

    @Query("select g  from Group g join  UserGroup ug on g.id = ug.groupId where ug.userId = ?1")
    List<Group> findGroupByUserId(String userId);
}
