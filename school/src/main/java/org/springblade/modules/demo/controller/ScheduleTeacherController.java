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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.demo.entity.ScheduleTeacher;
import org.springblade.modules.demo.vo.ScheduleTeacherVO;
import org.springblade.modules.demo.service.IScheduleTeacherService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.Collection;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2020-12-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/scheduleteacher")
@Api(value = "", tags = "接口")
public class ScheduleTeacherController extends BladeController {

	private final IScheduleTeacherService scheduleTeacherService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入scheduleTeacher")
	public R<ScheduleTeacher> detail(ScheduleTeacher scheduleTeacher) {
		ScheduleTeacher detail = scheduleTeacherService.getOne(Condition.getQueryWrapper(scheduleTeacher));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入scheduleTeacher")
	public R<IPage<ScheduleTeacher>> list(ScheduleTeacher scheduleTeacher, Query query) {
		IPage<ScheduleTeacher> pages = scheduleTeacherService.page(Condition.getPage(query), Condition.getQueryWrapper(scheduleTeacher));
		return R.data(pages);
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入scheduleTeacher")
	public R<IPage<ScheduleTeacherVO>> page(ScheduleTeacherVO scheduleTeacher, Query query) {
		IPage<ScheduleTeacherVO> pages = scheduleTeacherService.selectScheduleTeacherPage(Condition.getPage(query), scheduleTeacher);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入scheduleTeacher")
	public R save(@Valid @RequestBody ScheduleTeacher scheduleTeacher) {
		return R.status(scheduleTeacherService.save(scheduleTeacher));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入scheduleTeacher")
	public R update(@Valid @RequestBody ScheduleTeacher scheduleTeacher) {
		return R.status(scheduleTeacherService.updateById(scheduleTeacher));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入scheduleTeacher")
	public R submit(@Valid @RequestBody ScheduleTeacher scheduleTeacher) {
		return R.status(scheduleTeacherService.saveOrUpdate(scheduleTeacher));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(scheduleTeacherService.removeByIds(Func.toLongList(ids)));
	}

	@PostMapping("/course")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "排课", notes = "传入参数")
	public R course(){
		Integer teacherId=1;
		List<ScheduleTeacherVO> list=scheduleTeacherService.getBaseMapper().getList(teacherId);
		if (CollectionUtils.isEmpty(list)){
			Boolean b=scheduleTeacherService.getBaseMapper().insertBaseData();
		}
		String[] classes={"1","2","3"};
		for(int i=0;i<classes.length;i++){

		}
		return null;
	}

}
