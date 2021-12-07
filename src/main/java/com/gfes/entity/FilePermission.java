package com.gfes.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 文件权限
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_permission")
public class FilePermission implements Serializable {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@Column(name = "id", nullable = false , length = 32)
	private String id;

	/**
	 * 文件id
	 * default value: null
	 */
	@Column(name = "file_id", nullable = false , length = 32)
	private String fileId;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = false , length = 32)
	private String userId;

	@Column(name = "username", nullable = false , length = 32)
	private String username;

	/**
	 * 权限编码,0-无权限，1-只读，2-只写
	 * default value: 1
	 */
	@Column(name = "access_code", nullable = false , length = 3)
	private Integer accessCode;

	/**
	 * 创建时间
	 * default value: null
	 */
	@Column(name = "create_time")
	private java.util.Date createTime;

	/**
	 * 创建人
	 * default value: null
	 */
	@Column(name = "creator_id", nullable = true , length = 32)
	private String creatorId;
}
