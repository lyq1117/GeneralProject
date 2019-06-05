package cn.kgc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.BlockMapper;
import cn.kgc.dao.ProjectMapper;
import cn.kgc.dao.UserMapper;
import cn.kgc.dao.UserProjectMapper;
import cn.kgc.pojo.Block;
import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserProject;
import cn.kgc.util.DateUtil;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private UserProjectMapper userProjectMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private BlockMapper blockMapper;
	
	@Override
	public List<Project> getProjectsByLeaderId(String leaderId) {
		return projectMapper.getListByLeaderId(leaderId);
	}

	@Override
	public int addProject(Project project) {
		return projectMapper.add(project);
	}

	@Override
	public int getIncrement() {
		return projectMapper.getIncrement();
	}

	@Override
	public Project getProjectById(int projectId) {
		return projectMapper.getById(projectId);
	}

	@Override
	public int updateProject(Project project) {
		return projectMapper.update(project);
	}

	@Override
	public List<User> getMembersOfProject(int projectId) {
		//查询用户-工程关联
		List<UserProject> userProjects = userProjectMapper.getByProjectId(projectId);
		//初始化成员集合
		List<User> members = new ArrayList<>();
		for (UserProject userProject : userProjects) {
			//根据用户名查询用户
			User user = userMapper.getByUsername(userProject.getUserId());
			members.add(user);
		}
		return members;
	}

	@Override
	public List<Project> getAllProjects() {
		return projectMapper.getAll();
	}

	@Override
	public Map<String, List<Project>> getProjectStatisticsSurvey() {
		
		List<Project> list = projectMapper.getAll();
		
		List<Project> notCompleteList = new ArrayList<>();//未完成集合
		List<Project> delayList = new ArrayList<>();//延期集合
		
		List<Project> completeList = new ArrayList<>();//完成集合
		for (Project project : list) {
			if(project.getStatus() == 2) {//状态2 完成的工程
				completeList.add(project);
				continue;
			}
			if(project.getStatus() == 0 && 
					getEndDate(project.getCreateTime(), project.getDuration()).getTime()
					>= DateUtil.getDate(getDateFormat(new Date())).getTime()) {//状态0 未完成的工程
				notCompleteList.add(project);
				continue;
			}
			if(getEndDate(project.getCreateTime(), project.getDuration()).getTime()
					< DateUtil.getDate(getDateFormat(new Date())).getTime()
					&& project.getStatus() == 0) {//延期的工程
				delayList.add(project);
			}
		}
		Map<String, List<Project>> map = new HashMap<>();
		map.put("complete", completeList);
		map.put("notComplete", notCompleteList);
		map.put("delay", delayList);
		
		return map;
	}
	
	/**
	 * 根据工程的创建时间和工期计算预计结束时间
	 * @param createTime
	 * @param duration
	 * @return
	 */
	public Date getEndDate(Date createTime, int duration){
		long millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		return new Date(createTime.getTime() + millSecondOfDay * duration);
	}
	
	/**
	 * 格式化日期成字符串  yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public String getDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	@Override
	public List<Map<String, String>> getEnterpriseNewAndCompleteProject(Date mondayDate) {
		List<Project> list = projectMapper.getAll();
		
		long millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		Date tuesdayDate = new Date(mondayDate.getTime() + millSecondOfDay*1);
		Date wednesdayDate = new Date(mondayDate.getTime() + millSecondOfDay*2);
		Date thursdayDate = new Date(mondayDate.getTime() + millSecondOfDay*3);
		Date fridayDate = new Date(mondayDate.getTime() + millSecondOfDay*4);
		Date saturdayDate = new Date(mondayDate.getTime() + millSecondOfDay*5);
		Date sundayDate = new Date(mondayDate.getTime() + millSecondOfDay*6);
		
		//一周中每天新增工程数目
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		int count7 = 0;
		
		//一周中每天完成的工程数目
		int count11 = 0;
		int count22 = 0;
		int count33 = 0;
		int count44 = 0;
		int count55 = 0;
		int count66 = 0;
		int count77 = 0;
		
		//遍历企业所有工程
		for (Project project : list) {
			if(project.getStatus() == 2) {
				Date realEndDate = getEndDate(project.getCreateTime(), project.getRealDuration());
				if(realEndDate.getTime() == mondayDate.getTime())
					count11++;
				else if(realEndDate.getTime() == tuesdayDate.getTime())
					count22++;
				else if(realEndDate.getTime() == wednesdayDate.getTime())
					count33++;
				else if(realEndDate.getTime() == thursdayDate.getTime())
					count44++;
				else if(realEndDate.getTime() == fridayDate.getTime())
					count55++;
				else if(realEndDate.getTime() == saturdayDate.getTime())
					count66++;
				else if(realEndDate.getTime() == sundayDate.getTime())
					count77++;
			}
			
			if(project.getCreateTime().getTime() == mondayDate.getTime())
				count1++;
			else if(project.getCreateTime().getTime() == tuesdayDate.getTime()) 
				count2++;
			else if(project.getCreateTime().getTime() == wednesdayDate.getTime())
				count3++;
			else if(project.getCreateTime().getTime() == thursdayDate.getTime()) 
				count4++;
				
			else if(project.getCreateTime().getTime() == fridayDate.getTime()) 
				count5++;
			else if(project.getCreateTime().getTime() == saturdayDate.getTime())
				count6++;
			else if(project.getCreateTime().getTime() == sundayDate.getTime())
				count7++;
		}
		
		List<Map<String, String>> result = new ArrayList<>();
		Map<String, String> map1 = new HashMap<>();
		map1.put("x", DateUtil.getStr(mondayDate));
		map1.put("create", String.valueOf(count1));
		map1.put("complete", String.valueOf(count11));
		
		Map<String, String> map2 = new HashMap<>();
		map2.put("x", DateUtil.getStr(tuesdayDate));
		map2.put("create", String.valueOf(count2));
		map2.put("complete", String.valueOf(count22));
		
		Map<String, String> map3 = new HashMap<>();
		map3.put("x", DateUtil.getStr(wednesdayDate));
		map3.put("create", String.valueOf(count3));
		map3.put("complete", String.valueOf(count33));
		
		Map<String, String> map4 = new HashMap<>();
		map4.put("x", DateUtil.getStr(thursdayDate));
		map4.put("create", String.valueOf(count4));
		map4.put("complete", String.valueOf(count44));
		
		Map<String, String> map5 = new HashMap<>();
		map5.put("x", DateUtil.getStr(fridayDate));
		map5.put("create", String.valueOf(count5));
		map5.put("complete", String.valueOf(count55));
		
		Map<String, String> map6 = new HashMap<>();
		map6.put("x", DateUtil.getStr(saturdayDate));
		map6.put("create", String.valueOf(count6));
		map6.put("complete", String.valueOf(count66));
		
		Map<String, String> map7 = new HashMap<>();
		map7.put("x", DateUtil.getStr(sundayDate));
		map7.put("create", String.valueOf(count7));
		map7.put("complete", String.valueOf(count77));
		
		result.add(map1);
		result.add(map2);
		result.add(map3);
		result.add(map4);
		result.add(map5);
		result.add(map6);
		result.add(map7);
		
		return result;
	}

	@Override
	public List<Map<String, String>> getProjectProgressStatistics() {
		//今日日期（用户下面计算工程实际工期）
		String todayStr = DateUtil.getStr(new Date());
		//获取企业所有工程
		List<Project> allProjects = projectMapper.getAll();
		//准备结果集合
		List<Map<String, String>> result = new ArrayList<>();
		//遍历所有工程
		for (Project project : allProjects) {
			Map<String, String> map = new HashMap<>();//一个map对应表格中一行数据
			map.put("projectName", project.getName());//projectName对应表格中data-field="projectName"
			map.put("createTime", DateUtil.getStr(project.getCreateTime()));//创建日期
			map.put("endTime", DateUtil.getStr(getEndDate(project.getCreateTime(), 
										project.getDuration())));//预计结束日期
			map.put("leader", "<span leaderId=\""+project.getUser().getUsername()+"\" leaderName=\""+project.getUser().getName()+"\" class=\"badge bg-green project_progress_statistics_leader\">"+project.getUser().getName().substring(0, 1)+"</span>");//负责人
			
			//执行中的工程
			if(project.getStatus() == 0) {
				//计算实际工期
				Date todayDate = DateUtil.getDate(todayStr);
				long millSecondOfDay = 24*60*60*1000;//一天毫秒数
				long realDuration = (todayDate.getTime()-project.getCreateTime().getTime()) / millSecondOfDay;//实际工期
				double percentage = (double)realDuration/(double)project.getDuration();//进度百分比
				System.out.println("er---------" + percentage + "---" + realDuration + "---" + project.getDuration());
				if(percentage>1) {
					map.put("status", "<small class=\"label label-danger\">延期</small>");
				}
				else if(percentage>0.8 && percentage<=1) {
					map.put("status", "<small class=\"label label-warning\">有风险</small>");
				}
				else {
					map.put("status", "");
				}
			}
			//废弃的工程
			else if(project.getStatus() == 1) {
				map.put("status", "<small class=\"label label-danger\">已废弃</small>");
			}
			//已完成的工程
			else if(project.getStatus() == 2) {
				map.put("status", "<small class=\"label label-success\">已完成</small>");
			}
			
			//查询工程下的任务集合
			List<Block> blocks = blockMapper.getByProjectId(project.getId());
			int notCompleteCount = 0;//待完成的任务数
			int completeCount = 0;//已完成的任务数
			for (Block block : blocks) {
				if(block.getStatus() == 0)
					notCompleteCount++;
				else if(block.getStatus() == 2)
					completeCount++;
			}
			int totalCount = notCompleteCount + completeCount;//总任务数
			
			map.put("notComplete", String.valueOf(notCompleteCount));//工程待完成任务数
			map.put("complete", String.valueOf(completeCount));//工程已完成任务数
			map.put("duration", String.valueOf(project.getDuration()));//预计工期
			map.put("projectProgress", "<div class=\"progress progress-sm active\"><div class=\"progress-bar progress-bar-success progress-bar-striped\" role=\"progressbar\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: "+((double)completeCount/(double)totalCount*100)+"%\"></div></div>");
			
			result.add(map);
		}
		return result;
	}

}
