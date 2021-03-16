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
 * @since 2021-02-08
 */
@Data
@TableName("t_grade")
@ApiModel(value = "Grade对象", description = "Grade对象")
public class Grade implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	* 层级
	*/
		@ApiModelProperty(value = "层级")
		private String level;
	private String pId;
	/**
	* 名称
	*/
		@ApiModelProperty(value = "名称")
		private String name;
	/**
	* 排序
	*/
		@ApiModelProperty(value = "排序")
		private Integer sort;


}
