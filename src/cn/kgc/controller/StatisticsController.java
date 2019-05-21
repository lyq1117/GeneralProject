package cn.kgc.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Block;
import cn.kgc.pojo.Project;
import cn.kgc.service.BlockService;
import cn.kgc.service.ProjectService;
import cn.kgc.service.SignService;
import cn.kgc.util.DateUtil;

@Controller
@RequestMapping(value="/page/statistics")
public class StatisticsController {
	
	@Resource
	private ProjectService projectService;//工程业务对象
	@Resource
	private BlockService blockService;//任务(工程小块)业务对象
	@Resource
	private SignService signService;//签到业务对象
	
	/**
	 * 获取企业工程概况(包括 待完成工程、延期工程、已完成工程、工程总数)
	 * @return
	 */
	@RequiresPermissions("statistics:getProjectSurvey")
	@ResponseBody
	@RequestMapping(value="/getProjectSurvey.do", method=RequestMethod.POST)
	public String getProjectSurvey() {
		
		Map<String, List<Project>> map = projectService.getProjectStatisticsSurvey();
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取企业每周新增工程和完成工程
	 * @param mondayDate
	 * @return
	 */
	@RequiresPermissions("statistics:getEnterpriseNewAndCompleteProject")
	@ResponseBody
	@RequestMapping(value="/getEnterpriseNewAndCompleteProject.do",
					method=RequestMethod.POST)
	public String getEnterpriseNewAndCompleteProject(@RequestParam String mondayDate) {
		Date mondayDateD = DateUtil.getDate(mondayDate);
		//获取企业一周内每日新增和完成工程的数目
		List<Map<String, String>> result = projectService.getEnterpriseNewAndCompleteProject(mondayDateD);
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取企业任务周报统计
	 * @param mondayDate
	 * @return
	 */
	@RequiresPermissions("statistics:getEnterpriseBlockWeekly")
	@ResponseBody
	@RequestMapping(value="/getEnterpriseBlockWeekly.do",
					method=RequestMethod.POST)
	public String getEnterpriseBlockWeekly(@RequestParam String mondayDate) {
		Date mondayDateD = DateUtil.getDate(mondayDate);
		Map<String, List<Block>> map = blockService.getEnterpriseBlockWeekly(mondayDateD);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取工程进度统计表格数据
	 * @return
	 */
	@RequiresPermissions("statistics:getProjectProgressStatistics")
	@ResponseBody
	@RequestMapping(value="/getProjectProgressStatistics.do",
					method=RequestMethod.GET)
	public String getProjectProgressStatistics() {
		List<Map<String, String>> list = projectService.getProjectProgressStatistics();
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 获取企业员工签到统计
	 * @param mondayDate
	 * @param firstDateOfMonth
	 * @param daysOfMonth
	 * @return
	 */
	@RequiresPermissions("statistics:getMemberSignStatistics")
	@ResponseBody
	@RequestMapping(value="/getMemberSignStatistics.do",
					method=RequestMethod.GET)
	public String getMemberSignStatistics(@RequestParam String mondayDate,
										  @RequestParam String firstDateOfMonth,
										  @RequestParam int daysOfMonth) {
		System.out.println(mondayDate + "---" + firstDateOfMonth + "---" + daysOfMonth);
		Date mondayDateD = DateUtil.getDate(mondayDate);
		Date firstDateOfMonthD = DateUtil.getDate(firstDateOfMonth);
		//查询成员签到统计
		List<Map<String, String>> list = signService.getMemberSignStatistics(mondayDateD, firstDateOfMonthD, daysOfMonth);
		return JSONArray.toJSONString(list);
	}

	/**
	 * 获取企业周出勤人数和缺勤人数
	 * @param mondayDate
	 * @return
	 */
	@RequiresPermissions("statistics:getWeeklyAttendanceAndNot")
	@ResponseBody
	@RequestMapping(value="/getWeeklyAttendanceAndNot.do",
					method=RequestMethod.POST)
	public String getWeeklyAttendanceAndNot(@RequestParam String mondayDate) {
		//参数转为日期
		Date mondayDateD = DateUtil.getDate(mondayDate);
		Map<String, Object> result = signService.getWeeklyAttendanceAndNot(mondayDateD);
		return JSON.toJSONString(result);
	}
	
}
