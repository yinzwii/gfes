package com.gfes.service;

import cn.hutool.core.date.DateUtil;
import com.gfes.entity.FileInfo;
import com.gfes.entity.Group;
import com.gfes.entity.User;
import com.gfes.repository.FileInfoRepository;
import com.gfes.repository.GroupRepository;
import org.springframework.stereotype.Service;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class FileInfoService {

    private final FileInfoRepository fileInfoRepository;

    private final GroupRepository groupRepository;

    public FileInfoService(FileInfoRepository fileInfoRepository, GroupRepository groupRepository) {
        this.fileInfoRepository = fileInfoRepository;
        this.groupRepository = groupRepository;
    }

    public List<FileInfo> queryDocListByGroupId(String id) {
        return this.fileInfoRepository.queryDocListByGroupId(id);
    }

    public List<FileInfo> queryFile() {
        return this.fileInfoRepository.queryFile();
    }

    public Vector<Vector> selectAllVexctor() {
        Vector<Vector> rows = new Vector<Vector>();
        List<Object[]>  list =  this.fileInfoRepository.selectFileList();
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public Vector<Vector> selectAllFileInfos(User user) {
        Vector<Vector> rows = new Vector<Vector>();
        List<Object[]>  list = new ArrayList<>();
        if (user.getType() == 0){
            list =  this.fileInfoRepository.selectFileList();
        }else {
            List<Group> groups = groupRepository.findGroupByUserId(user.getId());
            if (groups == null || groups.isEmpty()){
                return rows;
            }
            List<FileInfo> files = groups.parallelStream()
                    .map(x -> fileInfoRepository.findByGroupId(x.getId()))
                    .filter(x -> x != null && !x.isEmpty())
                    .distinct()
                    .reduce(new ArrayList<>(), (a, b) -> {
                        a.addAll(b);
                        return a;
                    })
                    .stream()
                    .map(x -> fileInfoRepository.findByPId(x.getId()))
                    .filter(x -> x != null && !x.isEmpty())
                    .reduce(new ArrayList<>(), (a, b) -> {
                            a.addAll(b);
                            return a;
                        });
            if (files.isEmpty()){
                return rows;
            }
            list = files.stream()
                    .map(x -> {
                        Object[] arr = new Object[4];
                        arr[0] = x.getId();
                        arr[1] = x.getFileName();
                        arr[2] = DateUtil.format(x.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                        arr[3] = "";
                        return arr;
                    })
                    .collect(Collectors.toList());

        }

        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public void insertById(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }

    public String queryDocByNameAndPName(String name, String pName) {
        return fileInfoRepository.queryDocByNameAndPName(name,pName);
    }

    public String selectGroupIdByName(DefaultMutableTreeNode selectionNode) {
        return fileInfoRepository.selectGroupIdByName(selectionNode);
    }

    public String selectIdByName(String selectionNode) {
        return fileInfoRepository.selectIdByName(selectionNode);
    }

    public Vector<Vector> selectListByPId(String patentId) {
        Vector<Vector> rows = new Vector<Vector>();
        List<Object[]>  list =  this.fileInfoRepository.selectListByPId(patentId);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public Vector<Vector> queryGroupFiles(String groupId) {
        Vector<Vector> rows = new Vector<Vector>();
        List<FileInfo> list =  this.fileInfoRepository.queryGroupFiles(groupId);
        if (!list.isEmpty()) {
            for (FileInfo fileInfo : list) {
                Vector temp = new Vector<String>();
                temp.add(fileInfo.getId());
                temp.add(fileInfo.getFileName());
                temp.add(DateUtil.format(fileInfo.getCreateTime(),"yyyy-MM-DD HH:mm:ss"));
                temp.add("");
                rows.add(temp);
            }
        }
        return rows;
    }

    public Vector<Vector> queryFileById(String id) {
        Vector<Vector> rows = new Vector<Vector>();
        List<Object[]>  list =  this.fileInfoRepository.queryFileById(id);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public boolean existFile(String pid,String filaName){
        return fileInfoRepository.countByPIdAndFileName(pid,filaName) >= 1;
    }

    public FileInfo queryFile(String id){
        return fileInfoRepository.findById(id).get();
    }

    public FileInfo selectFileInfoById(String id) {
        return fileInfoRepository.selectFileInfoById(id);
    }

    public String queryFileByFileNameAndPid(String fileName, String pid) {
        return fileInfoRepository.queryFileByFileNameAndPid(fileName,pid);
    }

    public void updateFile(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }

    public int deleteFileById(String id) {
        if (fileInfoRepository.countByPId(id) == 0){
            try {
                fileInfoRepository.deleteById(id);
                return 1;
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}
