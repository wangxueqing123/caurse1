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
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.demo.entity.Score;
import org.springblade.modules.demo.vo.ScoreVO;
import org.springblade.modules.demo.service.IScoreService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 学生成绩 控制器
 *
 * @author BladeX
 * @since 2020-12-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/score")
@Api(value = "学生成绩", tags = "学生成绩接口")
public class ScoreController extends BladeController {

	private final IScoreService scoreService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入score")
	public R<Score> detail(Score score) {
		Score detail = scoreService.getOne(Condition.getQueryWrapper(score));
		return R.data(detail);
	}

	/**
	 * 分页 学生成绩
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入score")
	public R<IPage<Score>> list(Score score, Query query) {
		IPage<Score> pages = scoreService.page(Condition.getPage(query), Condition.getQueryWrapper(score));
		return R.data(pages);
	}

	/**
	 * 自定义分页 学生成绩
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入score")
	public R<IPage<ScoreVO>> page(ScoreVO score, Query query) {
		IPage<ScoreVO> pages = scoreService.selectScorePage(Condition.getPage(query), score);
		return R.data(pages);
	}

	/**
	 * 新增 学生成绩
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入score")
	public R save(@Valid @RequestBody Score score) {
		return R.status(scoreService.save(score));
	}

	/**
	 * 修改 学生成绩
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入score")
	public R update(@Valid @RequestBody Score score) {
		return R.status(scoreService.updateById(score));
	}

	/**
	 * 新增或修改 学生成绩
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入score")
	public R submit(@Valid @RequestBody Score score) {
		return R.status(scoreService.saveOrUpdate(score));
	}


	/**
	 * 删除 学生成绩
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(scoreService.deleteLogic(Func.toLongList(ids)));
	}

	@PostMapping("/getScore")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "成绩", notes = "传入id")
	public R getScore(Integer id){
		return R.data(scoreService.getBaseMapper().selectScoreById(id));
	}

}
