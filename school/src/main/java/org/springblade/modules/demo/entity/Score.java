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
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 学生成绩实体类
 *
 * @author BladeX
 * @since 2020-12-25
 */
@Data
@TableName("t_score")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Score对象", description = "学生成绩")
public class Score extends BaseEntity {

	private static final long serialVersionUID = 1L;

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
	/**
	* 语文
	*/
		@ApiModelProperty(value = "语文")
		private String chinese;
	/**
	* 数学
	*/
		@ApiModelProperty(value = "数学")
		private String math;
	/**
	* 英语
	*/
		@ApiModelProperty(value = "英语")
		private String english;
	/**
	* 政治
	*/
		@ApiModelProperty(value = "政治")
		private String politic;
	/**
	* 历史
	*/
		@ApiModelProperty(value = "历史")
		private String history;
	/**
	* 地理
	*/
		@ApiModelProperty(value = "地理")
		private String geography;
	/**
	* 物理
	*/
		@ApiModelProperty(value = "物理")
		private String physic;
	/**
	* 化学
	*/
		@ApiModelProperty(value = "化学")
		private String chemistry;
	/**
	* 生物
	*/
		@ApiModelProperty(value = "生物")
		private String biology;
	/**
	* 理综
	*/
		@ApiModelProperty(value = "理综")
		private String rationalization;
	/**
	* 文综
	*/
		@ApiModelProperty(value = "文综")
		private String comprehensive;


}
