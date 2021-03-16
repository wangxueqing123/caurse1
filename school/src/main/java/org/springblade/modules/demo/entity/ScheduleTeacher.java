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
 * 实体类
 *
 * @author BladeX
 * @since 2020-12-25
 */
@Data
@TableName("t_schedule_teacher")
@ApiModel(value = "ScheduleTeacher对象", description = "ScheduleTeacher对象")
public class ScheduleTeacher implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 教师编号
	*/
		@ApiModelProperty(value = "教师编号")
		private Integer id;
	/**
	* 星期
	*/
		@ApiModelProperty(value = "星期")
		private String weekday;

	/**
	* 第一节
	*/
		@ApiModelProperty(value = "第一节")
		private String one;
	/**
	* 第二节
	*/
		@ApiModelProperty(value = "第二节")
		private String two;
	/**
	* 第三节
	*/
		@ApiModelProperty(value = "第三节")
		private String three;
	/**
	* 第四节
	*/
		@ApiModelProperty(value = "第四节")
		private String four;
	/**
	* 第五节
	*/
		@ApiModelProperty(value = "第五节")
		private String five;
	/**
	* 第六节
	*/
		@ApiModelProperty(value = "第六节")
		private String six;
	/**
	* 第七节
	*/
		@ApiModelProperty(value = "第七节")
		private String seven;
	/**
	* 第八节
	*/
		@ApiModelProperty(value = "第八节")
		private String eight;
	/**
	* 第九节
	*/
		@ApiModelProperty(value = "第九节")
		private String nine;
	/**
	* 第十节
	*/
		@ApiModelProperty(value = "第十节")
		private String ten;
	/**
	* 姓名
	*/
		@ApiModelProperty(value = "姓名")
		private String name;
	/**
	* 学科
	*/
		@ApiModelProperty(value = "学科")
		private String subject;


}
