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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.demo.entity.*;
import org.springblade.modules.demo.service.*;
import org.springblade.modules.demo.service.impl.GradeServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.demo.vo.CaurseVO;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 排课 控制器
 *
 * @author BladeX
 * @since 2021-01-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/caurse")
@Api(value = "排课", tags = "排课接口")
public class CaurseController extends BladeController {

	private final ICaurseService caurseService;
	private final IGradeService gradeService;
	private final ICaurseplanService caurseplanService;
	private final IScheduleClassService scheduleClassService;
	private final IScheduleTeacherService scheduleTeacherService;
	private final ISubjectService subjectService;
	private final ITeacherService teacherService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入caurse")
	public R<Caurse> detail(Caurse caurse) {
		Caurse detail = caurseService.getOne(Condition.getQueryWrapper(caurse));
		return R.data(detail);
	}

	/**
	 * 分页 排课
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入caurse")
	public R<IPage<Caurse>> list(Caurse caurse, Query query) {
		IPage<Caurse> pages = caurseService.page(Condition.getPage(query), Condition.getQueryWrapper(caurse));
		return R.data(pages);
	}

	/**
	 * 自定义分页 排课
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入caurse")
	public R<IPage<CaurseVO>> page(CaurseVO caurse, Query query) {
		IPage<CaurseVO> pages = caurseService.selectCaursePage(Condition.getPage(query), caurse);
		return R.data(pages);
	}

	/**
	 * 新增 排课
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入caurse")
	public R save(@Valid @RequestBody Caurse caurse) {
		return R.status(caurseService.save(caurse));
	}

	/**
	 * 修改 排课
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入caurse")
	public R update(@Valid @RequestBody Caurse caurse) {
		return R.status(caurseService.updateById(caurse));
	}

	/**
	 * 新增或修改 排课
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入caurse")
	public R submit(@Valid @RequestBody Caurse caurse) {
		return R.status(caurseService.saveOrUpdate(caurse));
	}


	/**
	 * 删除 排课
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(caurseService.removeByIds(Func.toLongList(ids)));
	}
	/**
	 * 排课算法接口，传入学期开始去查对应学期的开课任务，进行排课，排完课程后添加到course_plan表
	 *
	 * @param semester
	 * @return
	 */
	@PostMapping("/caurse")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "排课", notes = "传入学期id")
	public R arrange(String semester) {
		return R.data(caurseService.classScheduling(semester));
	}

	@PostMapping("/getSemester")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "获取学期")
	public R getSemester() {
		QueryWrapper<Caurse> queryWrapper = new QueryWrapper<Caurse>().select("semester").groupBy("semester");
		List<Caurse> list = caurseService.list(queryWrapper);

		return R.data(list);
	}


	@PostMapping("/getGrade")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "获取年级")
	public R getGrade(String semester) {
		QueryWrapper<Caurse> queryWrapper = new QueryWrapper<Caurse>().eq("semester",semester).select("grade_no").groupBy("grade_no");
		List<Caurse> list = caurseService.list(queryWrapper);
		List<Grade> gList = new ArrayList<>();
		list.forEach(caurse->{
			QueryWrapper<Grade> queryWrapper1 = new QueryWrapper<Grade>().eq("id",caurse.getGradeNo());
			Grade grade= gradeService.getOne(queryWrapper1);
			gList.add(grade);
		});

		return R.data(gList);
	}

	@PostMapping("/getClass")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取班级")
	public R getClass( String grade) {
		QueryWrapper<Grade> queryWrapper = new QueryWrapper<Grade>().eq("p_id",grade);
		List<Grade> gradeList = gradeService.list(queryWrapper);

		return R.data(gradeList);
	}
	@PostMapping("/getCourse")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取班级课程表")
	public R getCourse(@RequestParam String classNo) {
		Boolean b=caurseService.getBaseMapper().deleteTable();
		int i = caurseService.getBaseMapper().insertBaseData();
		List<ScheduleClass> list1 = scheduleClassService.list();
		//获取选取班级的所有排课计划
		QueryWrapper<Caurseplan> queryWrapper = new QueryWrapper<Caurseplan>().eq("class_no",classNo);
		List<Caurseplan> list = caurseplanService.list(queryWrapper);
		list.forEach(caurseplan -> {
			//根据课程编号查询课程名称
			QueryWrapper<Subject> queryWrapper1 = new QueryWrapper<Subject>().eq("id",caurseplan.getCourseNo());
			Subject subject = subjectService.getOne(queryWrapper1);
			//获取课程计划中的上课时间
			String classTime = caurseplan.getClassTime();
			int i1 = Integer.parseInt(classTime)/10;
			int i2 = Integer.parseInt(classTime)%10;
			if (i2!=0){
				ScheduleClass scheduleClass = list1.get(i1);
				switch (i2){
					case 1:
						scheduleClass.setOne(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 2:
						scheduleClass.setTwo(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 3:
						scheduleClass.setThree(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 4:
						scheduleClass.setFour(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 5:
						scheduleClass.setFive(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 6:
						scheduleClass.setSix(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 7:
						scheduleClass.setSeven(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 8:
						scheduleClass.setEight(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
					case 9:
						scheduleClass.setNine(subject.getSubject());
						scheduleClassService.saveOrUpdate(scheduleClass);
						break;
				}
			}else if (i2==0){
				ScheduleClass scheduleClass = list1.get(i1-1);
				scheduleClass.setTen(subject.getSubject());
				scheduleClassService.saveOrUpdate(scheduleClass);
			}

		});
		return R.data(list1);
	}
	@PostMapping("/getTeacherCourse")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取班级课程表")
	public R getTeacherCourse(@RequestParam String teacherNo) {
		Boolean b=caurseService.getBaseMapper().deleteTeacherCourse();
		int i = caurseService.getBaseMapper().insertBaseData1();
		List<ScheduleTeacher> list1 = scheduleTeacherService.list();
		//获取所选教师的所有排课计划
		QueryWrapper<Caurseplan> queryWrapper = new QueryWrapper<Caurseplan>().eq("teacher_no",teacherNo);
		List<Caurseplan> list = caurseplanService.list(queryWrapper);
		list.forEach(caurseplan -> {
			//根据班级编号查询班级
			QueryWrapper<Grade> queryWrapper1 = new QueryWrapper<Grade>().eq("id",caurseplan.getClassNo());
			Grade grade = gradeService.getOne(queryWrapper1);
			//获取课程计划中的上课时间
			String classTime = caurseplan.getClassTime();
			int i1 = Integer.parseInt(classTime)/10;
			int i2 = Integer.parseInt(classTime)%10;
			if (i2!=0){
				ScheduleTeacher scheduleTeacher = list1.get(i1);
				switch (i2){
					case 1:
						scheduleTeacher.setOne(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 2:
						scheduleTeacher.setTwo(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 3:
						scheduleTeacher.setThree(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 4:
						scheduleTeacher.setFour(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 5:
						scheduleTeacher.setFive(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 6:
						scheduleTeacher.setSix(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 7:
						scheduleTeacher.setSeven(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 8:
						scheduleTeacher.setEight(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
					case 9:
						scheduleTeacher.setNine(grade.getName());
						scheduleTeacherService.saveOrUpdate(scheduleTeacher);
						break;
				}
			}else if (i2==0){
				ScheduleTeacher scheduleTeacher = list1.get(i1-1);
				scheduleTeacher.setTen(grade.getName());
				scheduleTeacherService.saveOrUpdate(scheduleTeacher);
			}

		});
		return R.data(list1);
	}

	@PostMapping("/getTeacher")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "教师信息")
	public R getTeacher(@RequestParam String teacherNo) {
		QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>().eq("id",teacherNo);
		return R.data(teacherService.getOne(queryWrapper));
	}
}
