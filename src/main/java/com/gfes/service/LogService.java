package com.gfes.service;

import com.gfes.entity.Log;
import com.gfes.repository.LogRepository;
import com.gfes.util.UuidUtil;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;


    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Page<Log> queryPage(Pageable pageable){
        return logRepository.findAll(pageable);
    }


    public Log createLog(Log log){
        log.setId(UuidUtil.idNoline());
        log.setCreateTime(new Date());
        return logRepository.save(log);
    }

    public List<Log> findByCondition(String keyWords) {
        return this.logRepository.findByCondition(keyWords);
    }
}
