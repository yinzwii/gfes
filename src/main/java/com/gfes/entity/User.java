package com.gfes.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 用户信息表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@Column(name = "id", nullable = false , length = 32)
	private String id;

	/**
	 * 用户名
	 * default value: null
	 */
	@Column(name = "name", nullable = false , length = 20)
	private String name;

	/**
	 * 密码
	 * default value: null
	 */
	@Column(name = "password", nullable = false , length = 64)
	private String password;

	/**
	 * 用户类型，0-超级管理员，1-管理员，2-普通用户
	 * default value: 2
	 */
	@Column(name = "type", nullable = false , length = 1)
	private Integer type;

	/**
	 * 个人密钥
	 * default value: null
	 */
	@Column(name = "secret", nullable = true , length = 64)
	private String secret;

	/**
	 * 私钥
	 * default value: null
	 */
	@Column(name = "private_key")
	private String privateKey;

	/**
	 * 公钥
	 * default value: null
	 */
	@Column(name = "public_key")
	private String publicKey;

	/**
	 * 创建时间
	 * default value: null
	 */
	@Column(name = "create_time", nullable = true )
	private java.util.Date createTime;
}
