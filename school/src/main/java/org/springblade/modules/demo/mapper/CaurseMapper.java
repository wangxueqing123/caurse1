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
package org.springblade.modules.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springblade.modules.demo.entity.Caurse;
import org.springblade.modules.demo.vo.CaurseVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 排课 Mapper 接口
 *
 * @author BladeX
 * @since 2021-01-08
 */

public interface CaurseMapper extends BaseMapper<Caurse> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param caurse
	 * @return
	 */
	List<CaurseVO> selectCaursePage(IPage page, CaurseVO caurse);

	@Select("SELECT distinct class_no FROM t_caurse")
	List<String> selectClassNo();

	@Select("SELECT distinct teacher_no FROM t_caurse")
	List<String> selectTeacherNo1();

	@Select("select distinct ${columnName} from t_caurse")
	List<String> selectByColumnName(String gradeNo);

	List<Caurse> selectTeacherNo (@Param("courseName") String courseName);

	Boolean deleteTable();

	Boolean deleteTeacherCourse();

	//班级课程表插入基础数据
	int insertBaseData();

	//教师课程表加入基础数据
	int insertBaseData1();


}
