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
import org.springblade.modules.demo.entity.Student;
import org.springblade.modules.demo.vo.StudentVO;
import org.springblade.modules.demo.service.IStudentService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 学生 控制器
 *
 * @author BladeX
 * @since 2020-12-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/student")
@Api(value = "学生", tags = "学生接口")
public class StudentController extends BladeController {

	private final IStudentService studentService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入student")
	public R<Student> detail(Student student) {
		Student detail = studentService.getOne(Condition.getQueryWrapper(student));
		return R.data(detail);
	}

	/**
	 * 分页 学生
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入student")
	public R<IPage<Student>> list(Student student, Query query) {
		IPage<Student> pages = studentService.page(Condition.getPage(query), Condition.getQueryWrapper(student));
		return R.data(pages);
	}

	/**
	 * 自定义分页 学生
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入student")
	public R<IPage<StudentVO>> page(StudentVO student, Query query) {
		IPage<StudentVO> pages = studentService.selectStudentPage(Condition.getPage(query), student);
		return R.data(pages);
	}

	/**
	 * 新增 学生
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入student")
	public R save(@Valid @RequestBody Student student) {
		return R.status(studentService.save(student));
	}

	/**
	 * 修改 学生
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入student")
	public R update(@Valid @RequestBody Student student) {
		return R.status(studentService.updateById(student));
	}

	/**
	 * 新增或修改 学生
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入student")
	public R submit(@Valid @RequestBody Student student) {
		return R.status(studentService.saveOrUpdate(student));
	}

	
	/**
	 * 删除 学生
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(studentService.removeByIds(Func.toLongList(ids)));
	}

	
}
