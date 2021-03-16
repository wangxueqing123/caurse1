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

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springblade.modules.demo.entity.Caurseplan;
import org.springblade.modules.demo.vo.CaurseplanVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 *  Mapper 接口
 *
 * @author BladeX
 * @since 2021-01-08
 */
public interface CaurseplanMapper extends BaseMapper<Caurseplan> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param caurseplan
	 * @return
	 */
	List<CaurseplanVO> selectCaurseplanPage(IPage page, CaurseplanVO caurseplan);

	@Insert("insert into t_caursePlan(grade_no, class_no, course_no, teacher_no, classroom_no, class_time, semester) values(#{grade_no}, #{class_no}, #{course_no}, #{teacher_no}, #{classroom_no}, #{class_time}, #{semester})")
	void insertCoursePlan(@Param("grade_no") String grade_no, @Param("class_no") String class_no, @Param("course_no") String course_no,
						  @Param("teacher_no") String teacher_no, @Param("classroom_no") String classroom_no, @Param("class_time") String class_time, @Param("semester") String semester);

	@Update("delete t_caursePlan")
	void deleteAllPlan();

}
