package com.gfes.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 文件日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log")
public class Log implements Serializable {

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
	@Column(name = "file_id", nullable = false , length = 64)
	private String fileId;

	/**
	 * 文件名称
	 * default value: null
	 */
	@Column(name = "file_name", nullable = false , length = 255)
	private String fileName;

	/**
	 * 操作（上传，打开，保存，关闭，删除）
	 * default value: null
	 */
	@Column(name = "operation", nullable = false , length = 32)
	private String operation;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = false , length = 32)
	private String userId;

	/**
	 * 用户名称
	 * default value: null
	 */
	@Column(name = "username", nullable = false , length = 64)
	private String username;

	/**
	 * 创建时间
	 * default value: null
	 */
	@Column(name = "create_time", nullable = false)
	private java.util.Date createTime;
}
