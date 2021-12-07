package com.gfes.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

/**
 * null
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`group`")
public class Group implements Serializable {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@Column(name = "id", nullable = false , length = 32)
	private String id;

	/**
	 * 名称
	 * default value: null
	 */
	@Column(name = "name", nullable = false , length = 255)
	private String name;

	/**
	 * 创建时间
	 * default value: null
	 */
	@Column(name = "create_time", nullable = true )
	private java.util.Date createTime;

	/**
	 * 排序
	 * default value: null
	 */
	@Column(name = "sort", nullable = true , length = 6)
	private Integer sort;


	@Override
	public String toString(){
		return this.getName() + "(组)";
	}
}
