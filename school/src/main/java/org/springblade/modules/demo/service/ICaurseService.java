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
package org.springblade.modules.demo.service;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.tool.api.R;
import org.springblade.modules.demo.entity.Caurse;
import org.springblade.modules.demo.mapper.CaurseMapper;
import org.springblade.modules.demo.vo.CaurseVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 排课 服务类
 *
 * @author BladeX
 * @since 2021-01-08
 */
public interface ICaurseService extends IService<Caurse> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param caurse
	 * @return
	 */
	CaurseMapper getBaseMapper();

	IPage<CaurseVO> selectCaursePage(IPage<CaurseVO> page, CaurseVO caurse);

	R classScheduling(@Param("semester") String semester);

}
