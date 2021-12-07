package com.gfes.repository;

import com.gfes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    User findByNameAndPassword(String name,String password);

    int countByName(String name);

    User findByName(String name);

    @Query(value = "select id,name,create_time,''  from user where 1 = 1" ,nativeQuery = true)
    List<Object[]> getUserList();


    @Query(value = "SELECT DISTINCT f.file_name,fp.username,fp.access_code,u.`name` creatorNmae\n" +
            "from file_permission  fp\n" +
            "LEFT JOIN `user` u on fp.creator_id = u.id\n" +
            "LEFT JOIN file_info f on f.id = fp.file_id WHERE file_id = ?1" ,nativeQuery = true)
    List<Object[]> selectListByFileId(String id);
}
