package com.gfes.service;

import com.gfes.entity.FilePermission;
import com.gfes.repository.FilePermissionRepository;
import com.gfes.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilePermissionService {

    private final FilePermissionRepository repository;

    public Page<FilePermission> queryPage(Pageable pageable){
        return repository.findAll(pageable);
    }


    public FilePermissionService(FilePermissionRepository repository) {
        this.repository = repository;
    }

//    public FilePermission addFilePermission(String userId,String fileId,int accessCode,String creatorId){
//        Optional<FilePermission> permissionOptional = repository.findByUserIdAndFileId(userId, fileId);
//        if (permissionOptional.isPresent()){
//            FilePermission filePermission = permissionOptional.get();
//            filePermission.setAccessCode(accessCode);
//            filePermission.setCreatorId(creatorId);
//            filePermission.setCreateTime(new Date());
//            return repository.save(filePermission);
//        }else {
//            FilePermission permission = FilePermission.builder()
//                    .accessCode(accessCode)
//                    .createTime(new Date())
//                    .creatorId(creatorId)
//                    .fileId(fileId)
//                    .id(UuidUtil.idNoline())
//                    .userId(userId)
//                    .build();
//            return repository.save(permission);
//        }
//    }
    public void addFilePermission(String userName ,String userId,String fileId,int accessCode,String creatorId){
        FilePermission filePermission = new FilePermission();
        filePermission.setAccessCode(accessCode);
        filePermission.setUserId(userId);
        filePermission.setFileId(fileId);
        filePermission.setCreatorId(creatorId);
        filePermission.setCreateTime(new Date());
        filePermission.setUsername(userName);
        filePermission.setId(UuidUtil.idNoline());
        this.repository.save(filePermission);
    }
    public void deletePermission(String id){
        repository.deleteById(id);
    }


    public FilePermission selectFilePermissionByCreatoId(String id) {
        return this.repository.selectFilePermissionByCreatoId(id);
    }

    public FilePermission selectFilePermissionByUserId(String id) {
        return this.repository.selectFilePermissionByUserId(id);
    }

    public void update(FilePermission filePermission) {
        this.repository.save(filePermission);
    }

    public FilePermission selectFilePermissionByUserIdAndFileId(String userId, String fileId) {
        return this.repository.selectFilePermissionByUserIdAndFileId(userId,fileId);
    }
}
