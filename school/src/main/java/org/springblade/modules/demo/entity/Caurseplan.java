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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2021-01-08
 */
@Data
@TableName("t_caurseplan")
@ApiModel(value = "Caurseplan对象", description = "Caurseplan对象")
public class Caurseplan implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
		@ApiModelProperty(value = "id")
		@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	* 年级编号
	*/
		@ApiModelProperty(value = "年级编号")
		private String gradeNo;
	/**
	* 班级编号
	*/
		@ApiModelProperty(value = "班级编号")
		private String classNo;
	/**
	* 课程编号
	*/
		@ApiModelProperty(value = "课程编号")
		private String courseNo;
	/**
	* 讲师编号
	*/
		@ApiModelProperty(value = "讲师编号")
		private String teacherNo;
	/**
	* 教室编号
	*/
		@ApiModelProperty(value = "教室编号")
		private String classroomNo;
	/**
	* 上课时间
	*/
		@ApiModelProperty(value = "上课时间")
		private String classTime;
	/**
	* 周数
	*/
		@ApiModelProperty(value = "周数")
		private Integer weeksSum;
	/**
	* 学期
	*/
		@ApiModelProperty(value = "学期")
		private String semester;
	private Boolean deleted;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;


}
