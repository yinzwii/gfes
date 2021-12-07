package com.gfes.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 用户-群组关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_group")
public class UserGroup implements Serializable {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@Column(name = "id", nullable = false , length = 32)
	private String id;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = false , length = 32)
	private String userId;

	/**
	 * 群组id
	 * default value: null
	 */
	@Column(name = "group_id", nullable = true , length = 32)
	private String groupId;

	/**
	 * 创建时间
	 * default value: null
	 */
	@Column(name = "create_time")
	private java.util.Date createTime;
}
