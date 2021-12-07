package com.gfes.repository;

import com.gfes.entity.FilePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

public interface FilePermissionRepository extends JpaRepository<FilePermission, String>{

    Optional<FilePermission> findByUserIdAndFileId(String userId,String fileId);

    @Query(value = "select * from file_permission where creator_id = ?1",nativeQuery = true)
    FilePermission selectFilePermissionByCreatoId(String id);

    @Query(value = "select * from file_permission where user_id = ?1",nativeQuery = true)
    FilePermission selectFilePermissionByUserId(String id);

    @Query(value = "select * from file_permission where user_id = ?1 and file_id = ?2",nativeQuery = true)
    FilePermission selectFilePermissionByUserIdAndFileId(String userId, String fileId);
}
