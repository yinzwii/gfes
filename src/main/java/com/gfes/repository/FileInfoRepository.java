package com.gfes.repository;

import com.gfes.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, String>, JpaSpecificationExecutor<FileInfo> {

    @Query(value = "SELECT * from file_info where doc = 1 and del_flag = 0" ,nativeQuery = true)
    List<FileInfo> queryDocListByGroupId(String id);

    @Query(value = "SELECT * from file_info where doc = 0 and del_flag = 0" ,nativeQuery = true)
    List<FileInfo> queryFile();

    @Query(value = "select id,file_name,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),''  from file_info where doc = 0 and del_flag = 0" ,nativeQuery = true)
    List<Object[]> selectFileList();

    @Query(value = "select id,file_name,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),'' as temp from file_info where 1=1 and del_flag='0'" ,nativeQuery = true)
    List<Object[]> selectList();

    @Query(value = "select DISTINCT f.id  \n" +
            "from file_info f\n" +
            "LEFT JOIN `group` g\n" +
            "on f.group_id = \n" +
            "(SELECT id \n" +
            "FROM `group` g\n" +
            "WHERE g.`name` = ?2)\n" +
            "where f.file_name = ?1 \n" +
            "and del_flag='0'" ,nativeQuery = true)
    String queryDocByNameAndPName(String fName, String pName);

    @Query(value = "select id from `group` WHERE name = ?1",nativeQuery = true)
    String selectGroupIdByName(DefaultMutableTreeNode selectionNode);


    @Query(value = "select id from file_info WHERE file_name = ?1",nativeQuery = true)
    String selectIdByName(String selectionNode);

    @Query(value = "select id,file_name,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),''  from file_info WHERE p_id = ?1",nativeQuery = true)
    List<Object[]> selectListByPId(String id);

    @Query(value = "select id,file_name,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s'),''  from file_info WHERE id = ?1",nativeQuery = true)
    List<Object[]> queryFileById(String id);

    @Query(value = "select *  from file_info WHERE id = ?1",nativeQuery = true)
    FileInfo selectFileInfoById(String id);

    @Query(value = "select id from file_info WHERE file_name = ?1 and p_id = ?2",nativeQuery = true)
    String queryFileByFileNameAndPid(String fileName, String pid);

    @Query("select ff from FileInfo f join FileInfo  ff on f.id = ff.PId where ff.doc = 0 and  f.doc = 1 and f.groupId = ?1")
    List<FileInfo> queryGroupFiles(String groupId);

    int countByPId(String pid);

    int countByPIdAndFileName(String pid,String fileName);

    List<FileInfo> findByPId(String pid);

    List<FileInfo> findByGroupId(String groupId);
}
