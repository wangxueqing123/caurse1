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
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 班级课程表实体类
 *
 * @author BladeX
 * @since 2021-01-27
 */
@Data
@TableName("t_schedule_class")
@ApiModel(value = "ScheduleClass对象", description = "班级课程表")
public class ScheduleClass implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* id
	*/
		@ApiModelProperty(value = "id")
		@TableId(value = "id", type = IdType.AUTO)
	private Long id;
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


}
