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
package org.springblade.modules.demo.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.demo.entity.Student;
import org.springblade.modules.demo.vo.StudentVO;
import java.util.Objects;

/**
 * 学生包装类,返回视图层所需的字段
 *
 * @author BladeX
 * @since 2020-12-15
 */
public class StudentWrapper extends BaseEntityWrapper<Student, StudentVO>  {

	public static StudentWrapper build() {
		return new StudentWrapper();
 	}

	@Override
	public StudentVO entityVO(Student student) {
		StudentVO studentVO = Objects.requireNonNull(BeanUtil.copy(student, StudentVO.class));

		//User createUser = UserCache.getUser(student.getCreateUser());
		//User updateUser = UserCache.getUser(student.getUpdateUser());
		//studentVO.setCreateUserName(createUser.getName());
		//studentVO.setUpdateUserName(updateUser.getName());

		return studentVO;
	}

}
