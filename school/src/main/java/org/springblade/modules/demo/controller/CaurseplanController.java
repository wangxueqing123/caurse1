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
import org.springblade.modules.demo.entity.Caurseplan;
import org.springblade.modules.demo.vo.CaurseplanVO;
import org.springblade.modules.demo.service.ICaurseplanService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2021-01-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("blade-demo/caurseplan")
@Api(value = "", tags = "接口")
public class CaurseplanController extends BladeController {

	private final ICaurseplanService caurseplanService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入caurseplan")
	public R<Caurseplan> detail(Caurseplan caurseplan) {
		Caurseplan detail = caurseplanService.getOne(Condition.getQueryWrapper(caurseplan));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入caurseplan")
	public R<IPage<Caurseplan>> list(Caurseplan caurseplan, Query query) {
		IPage<Caurseplan> pages = caurseplanService.page(Condition.getPage(query), Condition.getQueryWrapper(caurseplan));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入caurseplan")
	public R<IPage<CaurseplanVO>> page(CaurseplanVO caurseplan, Query query) {
		IPage<CaurseplanVO> pages = caurseplanService.selectCaurseplanPage(Condition.getPage(query), caurseplan);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入caurseplan")
	public R save(@Valid @RequestBody Caurseplan caurseplan) {
		return R.status(caurseplanService.save(caurseplan));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入caurseplan")
	public R update(@Valid @RequestBody Caurseplan caurseplan) {
		return R.status(caurseplanService.updateById(caurseplan));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入caurseplan")
	public R submit(@Valid @RequestBody Caurseplan caurseplan) {
		return R.status(caurseplanService.saveOrUpdate(caurseplan));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(caurseplanService.removeByIds(Func.toLongList(ids)));
	}

	
}
