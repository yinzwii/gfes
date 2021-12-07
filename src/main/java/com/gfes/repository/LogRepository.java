package com.gfes.repository;

import com.gfes.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, String>, JpaSpecificationExecutor<Log> {


//    @Query(value = "select * from log WHERE file_name like '%?2%' ",nativeQuery = true)
//    Page<Log> queryPageAndKeywords(Pageable pageable, String keyWords);

    @Query(value = "select * from log WHERE file_name like %?1% limit 10 ",nativeQuery = true)
    List<Log> findByCondition(String keyWords);
}
