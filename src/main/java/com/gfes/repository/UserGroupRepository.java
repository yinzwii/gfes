package com.gfes.repository;

import com.gfes.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, String>{

    @Transactional
    @Modifying
    int deleteByUserIdAndGroupId(String userId,String groupId);

    @Query(value = "select * from user_group WHERE user_id = ?1",nativeQuery = true)
    List<UserGroup> selectGroupIdByUserId(String id);

    @Query(value = "select * from user_group WHERE group_id = ?1",nativeQuery = true)
    List<UserGroup>  selectGroupUserByGroupId(String groupId);

    @Query(value = "select u.id , u.`name` userName, g.`name` groupName, g.createTime\n" +
            "from user_group ug \n" +
            "LEFT JOIN user u on u.id = ug.user_id\n" +
            "LEFT JOIN `group` g on ug.group_id = g.id\n" +
            "WHERE ug.user_id = ?1\n" +
            "and ug.group_id = ?2",nativeQuery = true)
    List<Object[]> selectListByUserIdAndGroupId(String userId, String groupId);
}
