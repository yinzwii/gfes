package com.gfes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`file_info`")
public class FileInfo  implements Serializable {

    @Id
    @Column(name = "id", nullable = false , length = 32)
    private String id;

    @Column(name = "group_id",length = 32)
    private String groupId;

    @Column(name = "file_name", nullable = false , length = 32)
    private String fileName;

    @Column(name = "file_path",length = 32)
    private String filePath;

    @Column(name = "type",length = 32)
    private String type;

    @Column(name = "size", length = 32)
    private Integer size;

    @Column(name = "create_time", nullable = false , length = 32)
    private Date createTime;

    @Column(name = "preview_path", length = 32)
    private String previewPath;

    @Column(name = "doc", nullable = false , length = 32)
    private int doc;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "p_id", length = 32)
    private String PId;

    @Column(name = "creator_id", length = 32)
    private String creatorId;

    @Column(name = "`lock`", length = 1)
    private Boolean lock;

    @Override
    public String toString(){
        return this.fileName;
    }
}
