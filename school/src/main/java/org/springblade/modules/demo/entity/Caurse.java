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
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 排课实体类
 *
 * @author BladeX
 * @since 2021-01-08
 */
@Data
@TableName("t_caurse")
@ApiModel(value = "Caurse对象", description = "排课")
public class Caurse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* id，即将要上课的，需要进行排课的
	*/
		@ApiModelProperty(value = "id，即将要上课的，需要进行排课的")
		@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	* 学期
	*/
		@ApiModelProperty(value = "学期")
		private String semester;
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
	* 课程名
	*/
		@ApiModelProperty(value = "课程名")
		private String courseName;
	/**
	* 教师编号
	*/
		@ApiModelProperty(value = "教师编号")
		private String teacherNo;
	/**
	* 教师姓名
	*/
		@ApiModelProperty(value = "教师姓名")
		private String realname;
	/**
	* 课程属性
	*/
		@ApiModelProperty(value = "课程属性")
		@TableField("courseAttr")
	private String courseAttr;
	/**
	* 学生人数
	*/
		@ApiModelProperty(value = "学生人数")
		@TableField("studentNum")
	private Integer studentNum;
	/**
	* 周数
	*/
		@ApiModelProperty(value = "周数")
		private Integer weeksSum;
	/**
	* 每周课程数
	*/
		@ApiModelProperty(value = "每周课程数")
		private Integer number;
	/**
	* 周学时
	*/
		@ApiModelProperty(value = "周学时")
		private Integer weeksNumber;
	/**
	* 是否固定上课时间
	*/
		@ApiModelProperty(value = "是否固定上课时间")
		@TableField("isFix")
	private String isFix;
	/**
	* 固定时间的话,2位为一个时间位置
	*/
		@ApiModelProperty(value = "固定时间的话,2位为一个时间位置")
		private String classTime;
	private Boolean deleted;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;


}
