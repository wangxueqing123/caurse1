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
import org.springblade.modules.demo.entity.Subject;
import org.springblade.modules.demo.vo.SubjectVO;
import org.springblade.modules.demo.service.ISubjectService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 学科 控制器
 *
 * @author BladeX
 * @since 2020-12-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/subject")
@Api(value = "学科", tags = "学科接口")
public class SubjectController extends BladeController {

	private final ISubjectService subjectService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入subject")
	public R<Subject> detail(Subject subject) {
		Subject detail = subjectService.getOne(Condition.getQueryWrapper(subject));
		return R.data(detail);
	}

	/**
	 * 分页 学科
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<IPage<Subject>> list(Subject subject, Query query) {
		IPage<Subject> pages = subjectService.page(Condition.getPage(query), Condition.getQueryWrapper(subject));
		return R.data(pages);
	}

	/**
	 * 自定义分页 学科
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入subject")
	public R<IPage<SubjectVO>> page(SubjectVO subject, Query query) {
		IPage<SubjectVO> pages = subjectService.selectSubjectPage(Condition.getPage(query), subject);
		return R.data(pages);
	}

	/**
	 * 新增 学科
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入subject")
	public R save(@Valid @RequestBody Subject subject) {
		return R.status(subjectService.save(subject));
	}

	/**
	 * 修改 学科
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入subject")
	public R update(@Valid @RequestBody Subject subject) {
		return R.status(subjectService.updateById(subject));
	}

	/**
	 * 新增或修改 学科
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入subject")
	public R submit(@Valid @RequestBody Subject subject) {
		return R.status(subjectService.saveOrUpdate(subject));
	}

	
	/**
	 * 删除 学科
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(subjectService.removeByIds(Func.toLongList(ids)));
	}

	
}
