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
package org.springblade.modules.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.utils.ClassUtil;
import org.springblade.core.tool.api.R;
import org.springblade.modules.demo.dto.CaurseDTO;
import org.springblade.modules.demo.entity.Caurse;
import org.springblade.modules.demo.entity.Caurseplan;
import org.springblade.modules.demo.entity.Grade;
import org.springblade.modules.demo.entity.Teacher;
import org.springblade.modules.demo.mapper.CaurseplanMapper;
import org.springblade.modules.demo.mapper.GradeMapper;
import org.springblade.modules.demo.mapper.TeacherMapper;
import org.springblade.modules.demo.vo.CaurseVO;
import org.springblade.modules.demo.mapper.CaurseMapper;
import org.springblade.modules.demo.service.ICaurseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.demo.vo.ConstantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 排课 服务实现类
 *
 * @author BladeX
 * @since 2021-01-08
 */
@Service
public class CaurseServiceImpl extends ServiceImpl<CaurseMapper, Caurse> implements ICaurseService {


	@Resource
	private CaurseMapper caurseMapper;

	@Resource
	private CaurseplanMapper caurseplanMapper;

	@Resource
	private TeacherMapper teacherMapper;

	@Resource
	private GradeMapper gradeMapper;

	// 不固定上课时间 1
	private final String UNFIXED_TIME = "unFixedTime";

	// 固定上课时间 2
	private final String IS_FIX_TIME = "isFixedTime";
	@Override
	public IPage<CaurseVO> selectCaursePage(IPage<CaurseVO> page, CaurseVO caurse) {
		return page.setRecords(baseMapper.selectCaursePage(page, caurse));
	}

	@Override
	@Transactional
	public R classScheduling(@Param("semester") String semester){

			//log.debug("开始排课,时间：" + System.currentTimeMillis());
			//long start = System.currentTimeMillis();
			//按学期获取排课任务
			QueryWrapper<Caurse> wrapper=new QueryWrapper<Caurse>().eq("semester",semester);
			List<Caurse> list=caurseMapper.selectList(wrapper);
			// 没有任务，排课失败
			if (list == null) {
				return R.fail("排课失败，查询不到排课任务！");
			}
			// 2、将开课任务的各项信息进行编码成染色体，分为固定时间与不固定时间
			List<Map<String, List<String>>>  geneList = coding(list,semester);
			// 3、给初始基因编码随机分配时间，得到同班上课时间不冲突的编码
			List<String> resultGeneList = codingTime(geneList);
			// 4、将分配好时间的基因编码以班级分类成为以班级的个体，得到班级的时间初始编码
			Map<String, List<String>> individualMap = transformIndividual(resultGeneList);
			//得到每个班级时间不冲突的编码
            individualMap = distinctTime(individualMap);
			// 5、遗传进化
			individualMap = geneticEvolution(individualMap);
			// 6、分配教室并做教室冲突检测
			//List<String> resultList = finalResult(individualMap);
			List<String> resultList = collectGene(individualMap);
			// 7、解码最终的染色体获取其中的基因信息
			List<Caurseplan> coursePlanList = decoding(resultList);
			// 8、写入tb_course_plan上课计划表
			caurseplanMapper.deleteAllPlan(); // 先删除原来的课程计划
			for (Caurseplan caurseplan : coursePlanList) {
				caurseplanMapper.insertCoursePlan(caurseplan.getGradeNo(), caurseplan.getClassNo(), caurseplan.getCourseNo(),
					caurseplan.getTeacherNo(), caurseplan.getClassroomNo(), caurseplan.getClassTime(), semester);
			}
			//log.debug("完成排课,耗时：" + (System.currentTimeMillis() - start));
			return R.success("排课成功！");


	}

	//得到时间不冲突的班级编码
	private Map<String, List<String>> distinctTime(Map<String, List<String>> individualMap) {

		for (String classNo : individualMap.keySet()) {
			// 得到每一个班级对应的基因编码
			List<String> individualList = individualMap.get(classNo);
			List<String> fixedGeneList = new ArrayList<>();
			List<String> unfixedGeneList = new ArrayList<>();
			List<Integer> list = new ArrayList<>();
			individualList.forEach(gene->{
				//固定上课时间的编码
				Integer isFix = Integer.parseInt(ClassUtil.cutGene(ConstantInfo.IS_FIX, gene));
				if (isFix==2){
						//获取已固定的时间段
						String classTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
						list.add(Integer.parseInt(classTime));
						fixedGeneList.add(gene);
				}else if (isFix==1){
					unfixedGeneList.add(gene);
				}
			});

			//获取1~70的不重复随机数
			List<Integer> list1 = ClassUtil.randomTime3();
			//将已固定的时间段移除
			list1.removeAll(list);
			List<String> newUnfixList = new ArrayList<>();
			for (int i=0;i<unfixedGeneList.size();i++){
				Integer number = list1.get(i);
				String time ;
				if (number < 10) {
					time = "0" + number;
				} else {
					time = "" + number;
				}
				String unFixGene = unfixedGeneList.get(i).substring(0,24)+time;
				newUnfixList.add(unFixGene);
			}
			individualList.clear();
			individualList.addAll(fixedGeneList);
			individualList.addAll(newUnfixList);
			individualMap.put(classNo,individualList);
		}
		return individualMap;
	}

	/**
	 * 编码规则:
	 *  固定时间：1
	 * 	年级编号：2
	 * 	班级编号：8
	 * 	讲师编号：5
	 * 	课程编号：6
	 * 	课程属性：2
	 * 	上课时间：2
	 * 	教室编号：6
	 *
	 * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+上课时间
	 * 其中如果不固定开课时间默认填充为"00"
	 * 经过处理后得到开课任务中
	 * @param
	 * @return List<String>
	 */
	private List<Map<String, List<String>>> coding(List<Caurse> caurseList,@Param("semester") String semster) {
		List<Map<String, List<String>>> geneList = new ArrayList<>();
		Map<String, List<String>> geneListMap = new HashMap<>();
		// 不固定1
		List<String> unFixedTimeGeneList = new ArrayList<>();
		// 固定2
		List<String> fixedTimeGeneList = new ArrayList<>();

		// 为语文课程设置软约束条件
		//QueryWrapper<Caurse> wrapper=new QueryWrapper<Caurse>().eq("course_name","语文").eq("semester",semster);
		List<Caurse> list1=caurseMapper.selectTeacherNo("语文");
		list1.forEach(teacher->{
			//QueryWrapper<Teacher> wrapper=new QueryWrapper<Teacher>().eq("id",teacherNo);
			String teacherNo = teacher.getTeacherNo();
			//根据教师编号查询出教师
			Teacher t=teacherMapper.selectById(teacherNo);
			QueryWrapper<Caurse> queryWrapper = new QueryWrapper<Caurse>().eq("teacher_no",teacherNo);
			List<Caurse> listT = caurseMapper.selectList(queryWrapper);
			//获取该教师的课程计划
			Caurse caurse= listT.get(0);
			String cnames = new String();
			cnames = t.getCnames();
			String[] classNo = new String[20];
			 classNo = cnames.split(",");
			 //获取该教师要教的所有班级，将周一三五为语文早读时每天的第一二节课程设置为语文课，每周二四六下午二三节课设置为作文课，每个班级设置一次
			 for (int i=0;i<classNo.length;i++){
			 	String cNo = classNo[i];
			 	QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<Grade>().eq("name",cNo);
				 Grade grade = gradeMapper.selectOne(gradeQueryWrapper);
				 String cId = grade.getId();
				 switch (i+1){
					case 1:
						//早读为语文的排两节课
						String gene1 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "01";
						String gene2 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "02";
						//作文课
						String gene7 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "16";
						String gene8 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "17";
						fixedTimeGeneList.add(gene1);
						fixedTimeGeneList.add(gene2);
						fixedTimeGeneList.add(gene7);
						fixedTimeGeneList.add(gene8);
						break;
					case 2:
						String gene3 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "21";

						String gene4 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "22";
						String gene9 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "36";
						String gene10 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "37";
						fixedTimeGeneList.add(gene3);
						fixedTimeGeneList.add(gene4);
						fixedTimeGeneList.add(gene9);
						fixedTimeGeneList.add(gene10);
						break;
					case 3:
						String gene5 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "41";

						String gene6 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "42";
						String gene11 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "56";

						String gene12 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "57";
						fixedTimeGeneList.add(gene5);
						fixedTimeGeneList.add(gene6);
						fixedTimeGeneList.add(gene11);
						fixedTimeGeneList.add(gene12);
						break;
				}

			 }

		});

		//为英语课程设置软约束条件
		List<Caurse> list2=caurseMapper.selectTeacherNo("英语");
		list2.forEach(teacher->{
			//QueryWrapper<Teacher> wrapper=new QueryWrapper<Teacher>().eq("id",teacherNo);
			String teacherNo = teacher.getTeacherNo();
			//根据教师编号查询出教师
			Teacher t=teacherMapper.selectById(teacherNo);
			QueryWrapper<Caurse> queryWrapper = new QueryWrapper<Caurse>().eq("teacher_no",teacherNo);
			List<Caurse> listT = caurseMapper.selectList(queryWrapper);
			//获取该教师的课程计划
			Caurse caurse= listT.get(0);
			String cnames = new String();
			cnames = t.getCnames();
			String[] classNo = new String[20];
			 classNo = cnames.split(",");
			 //获取该教师要教的所有班级，将周二四六为英语早读时每天的第一二节课程设置为英语课
			 for (int i=0;i<classNo.length;i++){
			 	String cNo = classNo[i];
				 QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<Grade>().eq("name",cNo);
				 Grade grade = gradeMapper.selectOne(gradeQueryWrapper);
				 String cId = grade.getId();
			 	switch (i+1){
					case 1:
						//早读为英语的排两节课
						String gene1 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "11";
						String gene2 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "12";
						fixedTimeGeneList.add(gene1);
						fixedTimeGeneList.add(gene2);
						break;
					case 2:
						String gene3 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "31";

						String gene4 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "32";
						fixedTimeGeneList.add(gene3);
						fixedTimeGeneList.add(gene4);
						break;
					case 3:
						String gene5 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "51";
						String gene6 = "2" + caurse.getGradeNo() + cId
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + "52";
						fixedTimeGeneList.add(gene5);
						fixedTimeGeneList.add(gene6);

						break;
				}

			 }

		});


		for (Caurse caurse : caurseList) {

			if (caurse.getIsFix().equals("1")) {
				// 获取每周的课程数
				int size = caurse.getNumber();
				//如果课程为语文，则每个班减去已经固定排了的四节课
				if (caurse.getCourseName().equals("语文")){
					for (int i = 0; i < size-4; i++) {
						// 编码:固定时间的课程
						String gene = caurse.getIsFix() + caurse.getGradeNo() + caurse.getClassNo()
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + ConstantInfo.DEFAULT_CLASS_TIME;
						unFixedTimeGeneList.add(gene);
					}
				}//如果课程为英语，则每个班减去两节固定排好的课
				else if (caurse.getCourseName().equals("英语")){
					for (int i = 0; i < size-2; i++) {
						// 编码:固定时间的课程
						String gene = caurse.getIsFix() + caurse.getGradeNo() + caurse.getClassNo()
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + ConstantInfo.DEFAULT_CLASS_TIME;
						unFixedTimeGeneList.add(gene);
					}
				}// 1，不固定上课时间，默认填充00
				else {
					for (int i = 0; i < size; i++) {
						// 编码:固定时间的课程
						String gene = caurse.getIsFix() + caurse.getGradeNo() + caurse.getClassNo()
							+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + ConstantInfo.DEFAULT_CLASS_TIME;
						unFixedTimeGeneList.add(gene);
					}
				}



			}
			// 2,固定上课时间
			if (caurse.getIsFix().equals("2")) {
				int size = caurse.getNumber();
				for (int i = 0; i < size; i++) {
					// 获得设定的固定时间
					String classTime = caurse.getClassTime();
					// 编码
					String gene = caurse.getIsFix() + caurse.getGradeNo() + caurse.getClassNo()
						+ caurse.getTeacherNo() + caurse.getCourseNo() + caurse.getCourseAttr() + classTime;
					fixedTimeGeneList.add(gene);
				}
			}
		}
		// 将两种上课时间的集合放入集合中
		geneListMap.put(UNFIXED_TIME, unFixedTimeGeneList);
		geneListMap.put(IS_FIX_TIME, fixedTimeGeneList);
		geneList.add(geneListMap);
		// 得到不含教室的初始基因编码
		return geneList;
	}

	/**
	 * 给初始基因编码随机分配时间(那些不固定上课时间的课程)
	 * @param geneList 固定时间与不固定时间的编码集合
	 * @return
	 */
	private List<String> codingTime(List<Map<String, List<String>>> geneList) {
		List<String> resultGeneList = new ArrayList<>();
		List<String> isFixedTimeGeneList = geneList.get(0).get(IS_FIX_TIME);
		List<String> unFixedTimeGeneList = geneList.get(0).get(UNFIXED_TIME);
		// 将固定上课时间的课程基因编码集合全部加入集合
		resultGeneList.addAll(isFixedTimeGeneList);
		// 没有固定时间的课程
		for (String gene : unFixedTimeGeneList) {
			// 获得一个随机时间
			//String classTime = ClassUtil.randomTime(gene, resultGeneList);
			// 得到分配好随机上课时间的基因编码
			gene = gene.substring(0, 24) + "00";
			// 分配好上课时间的编码集合
			resultGeneList.add(gene);
		}
		return resultGeneList;
	}

	//
	private List<String> randomTime(List<Map<String, List<String>>> geneList) {
		List<String> resultGeneList = new ArrayList<>();
		List<String> isFixedTimeGeneList = geneList.get(0).get(IS_FIX_TIME);
		List<String> unFixedTimeGeneList = geneList.get(0).get(UNFIXED_TIME);

		List<Integer> list = new ArrayList<>();
		// 将固定上课时间的课程基因编码集合全部加入集合
		resultGeneList.addAll(isFixedTimeGeneList);
		isFixedTimeGeneList.forEach(fixGene->{
			//获取已固定的时间段
			String classTime = ClassUtil.cutGene(ConstantInfo.IS_FIX, fixGene);
			list.add(Integer.parseInt(classTime));
		});
		//获取1~70的不重复随机数
		List<Integer> list1 = ClassUtil.randomTime3();
		//将已固定的时间段移除
		list1.removeAll(list);
		for (int i=0;i<unFixedTimeGeneList.size();i++){
			Integer number = list1.get(i);
			String time ;
			if (number < 10) {
				time = "0" + number;
			} else {
				time = "" + number;
			}
			String unFixGene = unFixedTimeGeneList.get(i).substring(0,24)+time;
			resultGeneList.add(unFixGene);
		}
		return resultGeneList;
	}

	/**
	 * 将初始基因编码(都分配好时间)划分以班级为单位的个体
	 * 班级编号的集合，去重
	 * @param resultGeneList
	 * @return
	 */
	private Map<String, List<String>> transformIndividual(List<String> resultGeneList) {
		Map<String, List<String>> individualMap = new HashMap<>();
		// 查询开课的班级
		List<String> classNoList = caurseMapper.selectClassNo();
		for (String classNo : classNoList) {
			List<String> geneList = new ArrayList<>();
			for (String gene : resultGeneList) {
				// 获得班级编号
				if (classNo.equals(ClassUtil.cutGene(ConstantInfo.CLASS_NO, gene))) {
					// 把含有该班级编号的基因编码加入集合
					geneList.add(gene);
				}
			}
			// 根据班级分配基因编码集合>1
			if (geneList.size() > 0) {
				individualMap.put(classNo, geneList);
			}
		}
		// 得到不同班级的初始课表
		return individualMap;
	}

	/**
	 * 遗传进化(每个班级中多条基因编码)
	 * 步骤：
	 * 1、初始化种群
	 * 2、交叉，选择
	 * 3、变异
	 * 4、重复2,3步骤
	 * 5、直到达到终止条件
	 * @param individualMap 按班级分的基因编码
	 * @return
	 */
	private Map<String, List<String>> geneticEvolution(Map<String, List<String>> individualMap) {
		// 遗传代数
		int generation = ConstantInfo.GENERATION;
		List<String> resultGeneList;
		for (int i = 0; i < generation; ++i) {
			// 1、选择、交叉individualMap：按班级分的课表
			individualMap = hybridization(individualMap);
			// 2、合拢所有班级的个体
			resultGeneList = collectGene(individualMap);
			// 2,3、变异
			//resultGeneList = geneMutation(collectGene(individualMap));
			// 4、冲突检测，消除冲突
//            conflictResolution(resultGeneList);
			// 5、将消除冲突后的个体再分班进入下一次进化
			//individualMap = transformIndividual(resultGeneList);
			individualMap = conflictResolution1(resultGeneList);
		}
		return individualMap;
	}

	/**
	 * 给每个班级交叉：一个班级看作一个种群
	 * @param individualMap
	 * @return
	 */
	private Map<String, List<String>> hybridization(Map<String, List<String>> individualMap) {
		// 对每一个班级的基因编码片段进行交叉
		for (String classNo : individualMap.keySet()) {
			// 得到每一个班级对应的基因编码
			List<String> individualList = individualMap.get(classNo);
			// 保存上一代
			List<String> oldIndividualList = individualList;
			// 交叉生成新个体,得到新生代
			individualList = selectGene(individualList);
			// 计算并对比子父代的适应度值，高的留下进行下一代遗传
			if (ClassUtil.calculatExpectedValue(individualList) >= ClassUtil.calculatExpectedValue(oldIndividualList)) {
				individualMap.put(classNo, individualList);
			} else {
				individualMap.put(classNo, oldIndividualList);
			}
		}
		return individualMap;
	}

	/**
	 * 个体中随机选择基因进行交叉(交换上课时间)
	 * @return
	 */
	private List<String> selectGene(List<String> individualList) {
		int min = 0;
		int max = individualList.size() - 1;
		boolean flag;
		do {
			// 从班级集合中随机选取两个坐标以便获得随机的两条基因编码
			int firstIndex = min + (int)(Math.random() * (max + 1 - min));
			int secondIndex = min + (int)(Math.random() * (max + 1 - min));
			// 获取随机基因编码
			String firstGene = individualList.get(firstIndex);
			String secondGene = individualList.get(secondIndex);
			if (firstIndex == secondIndex) {
				flag = false;
			} else if(ClassUtil.cutGene(ConstantInfo.IS_FIX, firstGene).equals("2") || ClassUtil.cutGene(ConstantInfo.IS_FIX, secondGene).equals("2")) {
				// 上课时间已经固定
				flag = false;
			} else {
				// 分别获取两条基因编码中的上课时间，开始交叉
				String firstClassTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, firstGene);
				String secondClassTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, secondGene);
				// 交换它们的上课时间
				firstGene = firstGene.substring(0, 24) + secondClassTime;
				secondGene = secondGene.substring(0, 24) + firstClassTime;
				// 将新得到的两条基因编码替换原来班级中的基因编码
				individualList.remove(firstIndex);
				individualList.add(firstIndex, firstGene);
				individualList.remove(secondIndex);
				individualList.add(secondIndex, secondGene);
				flag = true;
			}
		} while (!flag);
		return individualList;
	}

	/**
	 * 重新合拢交叉后的个体,即不分班级的基因编码，得到所有的编码
	 * @param individualMap
	 * @return
	 */
	private List<String> collectGene(Map<String, List<String>> individualMap) {
		List<String> resultList = new ArrayList<>();
		for (List<String> individualList : individualMap.values()) {
			resultList.addAll(individualList);
		}
		return resultList;
	}

	/**
	 * 基因变异
	 * @param resultGeneList 所有的基因编码
	 * @return
	 */
//	private List<String> geneMutation(List<String> resultGeneList) {
//		int min = 0;
//		int max = resultGeneList.size() - 1;
//		// 变异率，需要合理设置，太低则不容易进化得到最优解；太高则容易失去种群原来的优秀解
//		double mutationRate = 0.005; //0.002  0.003  0.004  0.005尽量设置低一些，0.01可能都大了
//		// 设定每一代中需要变异的基因个数，基因数*变异率
//		int mutationNumber = (int)(resultGeneList.size() * mutationRate);
//
//		if (mutationNumber < 1) {
//			mutationNumber = 1;
//		}
//
//		for (int i = 0; i < mutationNumber; ) {
//
//			int temp = min + (int)(Math.random() * (max + 1 - min));
//			// 随机拿一条编码
//			String gene = resultGeneList.get(temp);
//			if (ClassUtil.cutGene(ConstantInfo.IS_FIX, gene).equals("2")) {
//				break;
//			} else {
//				// 再随机给它一个上课时间
//				String newClassTime = ClassUtil.randomTime(gene, resultGeneList);
//				gene = gene.substring(0, 24) + newClassTime;
//				// 去掉原来的个体
//				resultGeneList.remove(temp);
//				// 原来位置上替换成新的个体
//				resultGeneList.add(temp, gene);
//				i = i + 1;
//			}
//		}
//		return resultGeneList;
//	}

	/**
	 * 冲突消除,同一个讲师同一时间上多门课。解决：重新分配一个时间，直到所有的基因编码中
	 * 不再存在上课时间冲突为止
	 * 因素：讲师-课程-时间-教室
	 * @param resultGeneList 所有个体集合
	 * @return
	 */
//	private List<String> conflictResolution(List<String> resultGeneList) {
//		int conflictTimes = 0;
//		eitx:
//		for (int i = 0; i < resultGeneList.size(); i++) {
//			// 得到集合中每一条基因编码的编码信息
//			String gene = resultGeneList.get(i);
//			String teacherNo = ClassUtil.cutGene(ConstantInfo.TEACHER_NO, gene);
//			String classTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
//			String classNo = ClassUtil.cutGene(ConstantInfo.CLASS_NO, gene);
//			for (int j = i + 1; j < resultGeneList.size(); j++) {
//				// 再找剩余的基因编码对比
//				String tempGene = resultGeneList.get(j);
//				String tempTeacherNo = ClassUtil.cutGene(ConstantInfo.TEACHER_NO, tempGene);
//				String tempClassTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, tempGene);
//				//String tempClassNo = ClassUtil.cutGene(ConstantInfo.CLASS_NO, tempGene);
//				// 冲突检测
//				if (classTime.equals(tempClassTime)) {
//					if ( teacherNo.equals(tempTeacherNo)) {
//						System.out.println("出现冲突情况");
//						conflictTimes ++;
//						String newClassTime = ClassUtil.randomTime(gene, resultGeneList);
//						String newGene = gene.substring(0, 24) + newClassTime;
//						resultGeneList = replace(resultGeneList, gene, newGene);
//						i = -1;
//						continue eitx;
//					}
//				}
////                // 判断是否有同一讲师同一时间上两门课
////                if (techerNo.equals(tempTecherNo) && classTime.equals(tempClassTime)) {
////                    // 说明同一讲师同一时间有两门以上的课要上，冲突出现，重新给这门课找一个时间
////                    String newClassTime = ClassUtil.randomTime(gene, resultGeneList);
////                    String newGene = gene.substring(0, 24) + newClassTime;
////                    resultGeneList = replace(resultGeneList, gene, newGene);
////                    continue eitx;
////                }
//			}
//		}
//		System.out.println("冲突发生次数:" + conflictTimes);
//		return resultGeneList;
//	}


	/*
	* 班级课程时间都不冲突，处理教师时间冲突*/
	private Map<String, List<String>> conflictResolution1(List<String> resultGeneList) {
		Map<String, List<String>> individualMap = new HashMap<>();
		Map<String, List<String>> individual = transformIndividual(resultGeneList);

		//获取开课的所有教师编号
		List<String> list = caurseMapper.selectTeacherNo1();

		list.forEach(teacherNo->{
			List<String> tList = new ArrayList<>();
			resultGeneList.forEach(gene->{
				//从编码中截取教师编号
				String tNo = ClassUtil.cutGene(ConstantInfo.TEACHER_NO, gene);

				if (tNo.equals(teacherNo)){
					tList.add(gene);
				}
				if (tList.size()>0){
					individualMap.put(teacherNo,tList);
				}
			});
		});
		//根据教师编号得到对应的所有编码
		for (String teacherNo:individualMap.keySet()){
			List<String> list1 = individualMap.get(teacherNo);
			for (int i=0;i<list1.size();i++){
				// 得到集合中每一条基因编码的编码信息
				String gene = list1.get(i);
				String classTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
				for (int j = i + 1; j < list1.size(); j++) {
					// 再找剩余的基因编码对比
					String tempGene = list1.get(j);
					String tempClassTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME, tempGene);
					// 冲突检测
					if (classTime.equals(tempClassTime)) {
						//根据冲突编码查询该条编码班级的所有编码
						List<String> list2=individual.get(ClassUtil.cutGene(ConstantInfo.CLASS_NO,tempGene));
						//随机获取一条编码
						Random random = new Random(list2.size());
						int num = random.nextInt(list2.size());
						String replaceGene = list2.get(num);
						//这条随机编码是非固定课程的话，进行替换
						if (ClassUtil.cutGene(ConstantInfo.IS_FIX, replaceGene)=="1"){
							String replaceTime = ClassUtil.cutGene(ConstantInfo.CLASS_TIME,replaceGene);
							String newTempGene = tempGene.substring(0,24)+replaceTime;
							String newReplaceGene = replaceGene.substring(0,24)+tempClassTime;
							//得到替换之后的新班级编码
							List<String> newList = new ArrayList<>();
							newList = replace(list2, replaceGene, newReplaceGene);
							newList = replace(list2, tempGene, newTempGene);
							if (ClassUtil.calculatExpectedValue(newList)>=ClassUtil.calculatExpectedValue(list2)){
								individual.put(ClassUtil.cutGene(ConstantInfo.CLASS_NO,tempGene),newList);
							}else {
								individual.put(ClassUtil.cutGene(ConstantInfo.CLASS_NO,tempGene),list2);
							}
						}

					}

				}
			}
		}
		return individual;
	}

	/**
	 * 替换基因编码
	 * @param resuleGeneList
	 * @param oldGene
	 * @param newGene
	 * @return
	 */
	private List<String> replace(List<String> resuleGeneList, String oldGene, String newGene) {
		for (int i = 0; i < resuleGeneList.size(); i++) {
			if (resuleGeneList.get(i).equals(oldGene)) {
				resuleGeneList.set(i, newGene);
				System.out.println("执行替换方法");
				return resuleGeneList;
			}
		}
		return resuleGeneList;
	}

	/**
	 * 开始给进化完的基因编码分配教室，即在原来的编码中加上教室编号，6
	 * @param individualMap
	 * @return
	 */
	private List<String> finalResult(Map<String, List<String>> individualMap) {
		// 存放编上教室编号的完整基因编码
		List<String> resultList = new ArrayList<>();
		// 将map集合中的基因编码再次全部混合
		List<String> resultGeneList = collectGene(individualMap);
		String classroomNo = "";
		// 得到课程任务的年级列表 01 02 03
		List<String> gradeList = caurseMapper.selectByColumnName(ConstantInfo.GRADE_NO);
		// 将基因编码按照年级分配
		Map<String, List<String>> gradeMap = collectGeneByGrade(resultGeneList, gradeList);
		// 这里需要根据安排教学区域时选的教学楼进行安排课程
		for (String gradeNo : gradeMap.keySet()) {
			// 找到当前年级对应的教学楼编号
//            String teachBuildNo = teachBuildInfoDao.selectBuildNo(gradeNo);
			// 如果一个年级设置多个教学楼，则需要使用List ===============>>>>>>>
//暂时			List<String> teachBuildNoList = teachBuildInfoDao.selectTeachBuildList(gradeNo);

			// 得到不同年级的课程基因编码
			List<String> gradeGeneList = gradeMap.get(gradeNo);

			// 看看对应教学楼下有多少教室，在设定的教学楼下开始随机分配教室
//            List<Classroom> classroomList = classroomDao.selectByTeachbuildNo(teachBuildNo);

			// 年级设置多个教学楼栋的情况
			QueryWrapper wrapper = new QueryWrapper();
//z			wrapper.in("teachbuild_no", teachBuildNoList);
//            List<Classroom> classroomList1 = classroomDao.selectByTeachbuildNoList(teachBuildNoList);

//z			List<Classroom> classroomList2 = classroomDao.selectList(wrapper);

			for (String gene : gradeGeneList) {
				// 分配教室
//z				classroomNo = issueClassroom(gene, classroomList2, resultList);
				// 基因编码中加入教室编号，至此所有基因信息编码完成，得到染色体
				gene = gene + classroomNo;
				// 将最终的编码加入集合中
				resultList.add(gene);
			}
		}
		// 完整的基因编码，即分配有教室的
		return resultList;
	}

	/**
	 * 将所有的基因编码按照年级分类
	 * @param resultGeneList
	 * @param gradeList
	 * @return
	 */
	private Map<String, List<String>> collectGeneByGrade(List<String> resultGeneList, List<String> gradeList) {
		Map<String, List<String>> map = new HashMap<>();
		for (String gradeNo : gradeList) {
			List<String> geneList = new ArrayList<>();
			// 找到基因编码集合中相应的年级并归类
			for (String gene : resultGeneList) {
				if (ClassUtil.cutGene(ConstantInfo.GRADE_NO, gene).equals(gradeNo)) {
					// 将当前的年级的基因编码加入集合
					geneList.add(gene);
				}
			}
			// 将当前年级对应的编码集合放入集合
			if (geneList.size() > 0) {
				map.put(gradeNo, geneList);
			}
		}
		// 得到不同年级的基因编码(年级，编码集合)
		return map;
	}



	/**
	 * 解码染色体中的基因，按照之前的编码解
	 * 编码:
	 *  固定时间：1
	 * 	年级编号：2
	 * 	班级编号：8
	 * 	讲师编号：5
	 * 	课程编号：6
	 * 	课程属性：2
	 * 	上课时间：2
	 * 	教室编号：6
	 * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+上课时间+教室编号(遗传算法执行完最后再分配教室)
	 * 其中如果不固定开课时间默认填充为"00"
	 * @param resultList 全部上课计划实体
	 * @return
	 */
	private List<Caurseplan> decoding(List<String> resultList) {
		List<Caurseplan> coursePlanList = new ArrayList<>();
		for (String gene : resultList) {
			Caurseplan caurseplan = new Caurseplan();
			// 年级
			caurseplan.setGradeNo(ClassUtil.cutGene(ConstantInfo.GRADE_NO, gene));
			// 班级
			caurseplan.setClassNo(ClassUtil.cutGene(ConstantInfo.CLASS_NO, gene));
			// 课程
			caurseplan.setCourseNo(ClassUtil.cutGene(ConstantInfo.COURSE_NO, gene));
			// 讲师
			caurseplan.setTeacherNo(ClassUtil.cutGene(ConstantInfo.TEACHER_NO, gene));
			// 教室
			caurseplan.setClassroomNo(ClassUtil.cutGene(ConstantInfo.CLASSROOM_NO, gene));
			// 上课时间
			caurseplan.setClassTime(ClassUtil.cutGene(ConstantInfo.CLASS_TIME, gene));
			coursePlanList.add(caurseplan);
		}
		return coursePlanList;
	}
}


