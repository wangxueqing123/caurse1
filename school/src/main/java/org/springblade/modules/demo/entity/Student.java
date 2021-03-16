/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 学生实体类
 *
 * @author BladeX
 * @since 2020-12-18
 */
@Data
@TableName("t_student")
@ApiModel(value = "Student对象", description = "学生")
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 学号
	*/
		@ApiModelProperty(value = "学号")
		private Long id;
	/**
	* 姓名
	*/
		@ApiModelProperty(value = "姓名")
		private String name;
	/**
	* 班级
	*/
		@ApiModelProperty(value = "班级")
		private String cname;
	/**
	* 年级
	*/
		@ApiModelProperty(value = "年级")
		private String grade;


}
