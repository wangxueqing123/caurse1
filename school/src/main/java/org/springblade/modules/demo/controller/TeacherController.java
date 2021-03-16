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
package org.springblade.modules.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.demo.entity.Teacher;
import org.springblade.modules.demo.vo.TeacherVO;
import org.springblade.modules.demo.service.ITeacherService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 教师 控制器
 *
 * @author BladeX
 * @since 2020-12-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/teacher")
@Api(value = "教师", tags = "教师接口")
public class TeacherController extends BladeController {

	private final ITeacherService teacherService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入teacher")
	public R<Teacher> detail(Teacher teacher) {
		Teacher detail = teacherService.getOne(Condition.getQueryWrapper(teacher));
		return R.data(detail);
	}

	/**
	 * 分页 教师
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入teacher")
	public R<IPage<Teacher>> list(Teacher teacher, Query query) {
		IPage<Teacher> pages = teacherService.page(Condition.getPage(query), Condition.getQueryWrapper(teacher));
		return R.data(pages);
	}

	/**
	 * 自定义分页 教师
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入teacher")
	public R<IPage<TeacherVO>> page(TeacherVO teacher, Query query) {
		IPage<TeacherVO> pages = teacherService.selectTeacherPage(Condition.getPage(query), teacher);
		return R.data(pages);
	}

	/**
	 * 新增 教师
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入teacher")
	public R save(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.save(teacher));
	}

	/**
	 * 修改 教师
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入teacher")
	public R update(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.updateById(teacher));
	}

	/**
	 * 新增或修改 教师
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入teacher")
	public R submit(@Valid @RequestBody Teacher teacher) {
		return R.status(teacherService.saveOrUpdate(teacher));
	}

	
	/**
	 * 删除 教师
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(teacherService.removeByIds(Func.toLongList(ids)));
	}

	
}
