package cn.kgc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.kgc.pojo.Block;
import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserBlock;
import cn.kgc.pojo.UserProject;
import cn.kgc.service.BlockService;
import cn.kgc.service.ProjectService;
import cn.kgc.service.UserBlockService;
import cn.kgc.service.UserProjectService;
import cn.kgc.service.UserService;
import cn.kgc.util.DateUtil;

@Controller
@RequestMapping("/page/task")
public class TaskController {
	
	@Resource
	private ProjectService projectService;
	@Resource
	private BlockService blockService;
	@Resource
	private UserProjectService userProjectService;
	@Resource
	private UserService userService;
	@Resource
	private UserBlockService userBlockService;
	
	/**
	 * 获取当前用户拥有的项目列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMyProjectList.do", method=RequestMethod.POST)
	public String getMyProject() {
		Subject subject = SecurityUtils.getSubject();
		//获取当前用户
		User user = (User) subject.getPrincipal();
		//根据用户名获取该用户为项目经理的项目集合
		List<Project> projects = projectService.getProjectsByLeaderId(user.getUsername());
		//初始化带有工程小块的工程List
		List<Map<String, String>> list = new ArrayList<>();
		for (Project project : projects) {
			Map<String, String> map = new HashMap();
			System.out.println("pro .... id ...----" + project.getId());
			List<Block> blocks = blockService.getBlocksByProjectId(project.getId());
			map.put("project", JSON.toJSONString(project));
			map.put("blocks", JSONArray.toJSONString(blocks));
			list.add(map);
		}
		Object object = JSONArray.toJSON(list);
		return object.toString();
	}
	
	/**
	 * 根据工程小块id获取团队集合
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUsersByBlockId.do", method=RequestMethod.POST)
	public String getUsersByBlockId(@RequestParam int blockId) {
		List<User> users = blockService.getUsersByBlockId(blockId);
		Object object = JSONArray.toJSON(users);
		return object.toString();
	}
	
	/**
	 * 更新工程小块的负责人
	 * @param username
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateBlockLeader.do", method=RequestMethod.POST)
	public String updateBlockLeader(@RequestParam String username,
									@RequestParam int blockId) {
		int result = blockService.updateBlockLeader(username, blockId);
		if(result == 1)
			return "true";
		else
			return "false";
	}
	
	/**
	 * 添加工程
	 * @param projectName
	 * @param createTime
	 * @param duration
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addProject.do")
	public String addProject(@RequestParam String projectName,
							 @RequestParam String createTime,
							 @RequestParam int duration,
							 @RequestParam String location,
							 @RequestParam double lng,
							 @RequestParam double lat) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		Project project = new Project();
		int idIncrement = projectService.getIncrement();
		project.setId(idIncrement+1);
		project.setName(projectName);
		project.setCreateTime(DateUtil.getDate(createTime));
		project.setDuration(duration);
		project.setUser(user);
		project.setStatus(0);
		project.setLocation(location);
		project.setLng(lng);
		project.setLat(lat);
		
		System.out.println("----**------" + location + "-" + lng + "-" + lat);
		
		int result = projectService.addProject(project);
		
		UserProject userProject = new UserProject(user.getUsername(), idIncrement+1);
		int result2 = userProjectService.add(userProject);
		
		if(result == 1 && result2 == 1)
			return "true";
		else
			return "false";
	}
	
	/**
	 * 根据工程id获取工程信息
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getProjectById.do")
	public String getProjectById(@RequestParam int projectId) {
		Project project = projectService.getProjectById(projectId);
		List<Block> blocks = blockService.getBlocksByProjectId(projectId);
		Map<String, String> map = new HashMap<>();
		map.put("project", JSON.toJSONString(project));
		map.put("blocks", JSONArray.toJSONString(blocks));
		return JSON.toJSONString(map);
	}
	
	/**
	 * 跟新工程信息
	 * @param projectId
	 * @param projectName
	 * @param projectDescription
	 * @param projectCreateTime
	 * @param projectDuration
	 * @param projectStatus
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateProject.do",method=RequestMethod.POST)
	public String updateProject(@RequestParam int projectId,
								@RequestParam String projectName,
								@RequestParam String projectDescription,
								@RequestParam String projectCreateTime,
								@RequestParam int projectDuration,
								@RequestParam int projectStatus) {
		Project project = new Project();
		project.setId(projectId);
		project.setName(projectName);
		project.setDescription(projectDescription);
		project.setCreateTime(DateUtil.getDate(projectCreateTime));
		project.setDuration(projectDuration);
		project.setStatus(projectStatus);
		//更新工程信息业务
		int result = projectService.updateProject(project);
		Map<String, String> map = new HashMap<>();
		if(result == 1) 
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取工程的成员集合
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMembersOfProject.do",method=RequestMethod.GET)
	public String getMembersOfProject(@RequestParam int projectId) {
		
		Project project = projectService.getProjectById(projectId);
		
		//成员结合
		List<User> members = projectService.getMembersOfProject(projectId);
		List<Map<String, String>> list = new ArrayList<>();
		for (User user : members) {
			Map<String, String>	map = new HashMap<>();
			map.put("username", user.getUsername());
			map.put("name", user.getName());
			map.put("tel", user.getTel());
			map.put("deptName", user.getDept().getName());
			if(project.getUser().getUsername().equals(user.getUsername()))
				map.put("option", "");
			else
				map.put("option", "<a class=\"my_project_detail_deleteBtn\" name=\""+ user.getUsername() +"\" href=\"javascript:void(0)\" style=\"color:#f56954\"><i class=\"fa fa-close\"></i></a>");
			list.add(map);
		}
		System.out.println(JSONArray.toJSONString(list));
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 获取不是工程成员的用户集合
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMembersNotInProject.do")
	public String getMembersNotInProject(@RequestParam int projectId) {
		//获取不是工程成员的用户集合
		List<User> users = userService.getUsersNotInProject(projectId);
		return JSONArray.toJSONString(users);
	}
	
	/**
	 * 添加用户-工程关联
	 * @param username
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addUserProject.do")
	public String addUserProject(@RequestParam String username,
								 @RequestParam int projectId) {
		UserProject userProject = new UserProject();
		userProject.setUserId(username);
		userProject.setProjectId(projectId);
		int result = userProjectService.add(userProject);
		Map<String, String> map = new HashMap<>();
		if(result == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除用户-工程关联
	 * @param username
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteUserProject.do")
	public String deleteUserProject(@RequestParam String username,
									@RequestParam int projectId) {
		UserProject userProject = new UserProject();
		userProject.setUserId(username);
		userProject.setProjectId(projectId);
		int result = userProjectService.delete(userProject);
		Map<String, String> map = new HashMap<>();
		if(result == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 根据id获取工程小块(即任务)信息
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBlockById.do")
	public String getBlockById(@RequestParam int blockId) {
		Block block = blockService.getBlockById(blockId);
		return JSON.toJSONString(block);
	}
	
	@ResponseBody
	@RequestMapping(value="/updateBlock.do")
	public String updateBlock(@RequestParam int blockId,
							  @RequestParam String description,
							  @RequestParam String createTime,
							  @RequestParam int duration,
							  @RequestParam int status) {
		Block block = new Block();
		block.setId(blockId);
		block.setDescription(description);
		block.setCreateTime(DateUtil.getDate(createTime));
		block.setDuration(duration);
		block.setStatus(status);
		int result = blockService.updateBlock(block);
		Map<String, String> map = new HashMap<>();
		if(result == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取任务成员
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMembersOfBlock.do")
	public String getMembersOfBlock(@RequestParam int blockId) {
		//根据id获取任务
		Block block = blockService.getBlockById(blockId);
		
		List<User> members = userService.getMembersOfBlock(blockId);
		List<Map<String, String>> list = new ArrayList<>();
		for (User user : members) {
			Map<String, String> map = new HashMap<>();
			map.put("username", user.getUsername());
			map.put("name", user.getName());
			map.put("tel", user.getTel());
			map.put("deptName", user.getDept().getName());
			if(block.getLeader().getUsername().equals(user.getUsername()))
				map.put("option", "");
			else
				map.put("option", "<a class=\"my_block_detail_deleteBtn\" name=\""+ user.getUsername() +"\" href=\"javascript:void(0)\" style=\"color:#f56954\"><i class=\"fa fa-close\"></i></a>");
			list.add(map);
		}
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 获取不是工程小块成员但是工程成员的用户集合
	 * @param blockId
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMembersNotInBlcokInProject.do")
	public String getMembersNotInBlcokInProject(@RequestParam int blockId,
												@RequestParam int projectId) {
		List<User> users = userService.getMembersNotInBlockInProject(blockId, projectId);
		return JSONArray.toJSONString(users);
	}
	
	/**
	 * 添加用户-任务关联
	 * @param username
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addUserBlock.do")
	public String addUserBlock(@RequestParam String username,
							   @RequestParam int blockId) {
		UserBlock userBlock = new UserBlock(username, blockId);
		int result = userBlockService.addUserBlock(userBlock);
		Map<String, String> map = new HashMap<>();
		if(result == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除用户-任务关联
	 * @param username
	 * @param blockId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteUserBlock.do")
	public String deleteUserBlock(@RequestParam String username,
								  @RequestParam int blockId) {
		UserBlock userBlock = new UserBlock(username, blockId);
		int result = userBlockService.deleteUserBlock(userBlock);
		Map<String, String> map = new HashMap<>();
		if(result == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 添加工程小块(任务)
	 * @param projectId
	 * @param description
	 * @param createTime
	 * @param duration
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addBlock.do")
	public String addBlock(@RequestParam int projectId,
						   @RequestParam String description,
						   @RequestParam String createTime,
						   @RequestParam int duration,
						   @RequestParam int status) {
		int idIncrement = blockService.getIncrement();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		Block block = new Block();
		block.setId(idIncrement + 1);
		block.setDescription(description);
		block.setCreateTime(DateUtil.getDate(createTime));
		block.setDuration(duration);
		block.setStatus(status);
		block.setProjectId(projectId);
		block.setLeader(user);
		int result = blockService.addBlock(block);
		
		UserBlock userBlock = new UserBlock(user.getUsername(), idIncrement+1);
		int result2 = userBlockService.addUserBlock(userBlock);
		
		Map<String, String> map = new HashMap<>();
		if(result == 1 && result2 == 1)
			map.put("result", "true");
		else
			map.put("result", "false");
		return JSON.toJSONString(map);
	}

	/**
	 * 获取当前用户的任务集合
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBlocksOfUser.do")
	public String getBlocksOfUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		List<UserBlock> userBlocks = userBlockService.getUserBlockByUserId(user.getUsername());
		List<Block> blocks = new ArrayList<>();
		for (UserBlock userBlock : userBlocks) {
			Block block = blockService.getBlockById(userBlock.getBlockId());
			blocks.add(block);
		}
		return JSONArray.toJSONString(blocks);
	}
	
	@ResponseBody
	@RequestMapping(value="/getAllProjects.do")
	public String getAllProjects() {
		
		List<Project> projects = projectService.getAllProjects();
		//初始化带有工程小块的工程List
		List<Map<String, String>> list = new ArrayList<>();
		for (Project project : projects) {
			Map<String, String> map = new HashMap();
			System.out.println("pro .... id ...----" + project.getId());
			List<Block> blocks = blockService.getBlocksByProjectId(project.getId());
			map.put("project", JSON.toJSONString(project));
			map.put("blocks", JSONArray.toJSONString(blocks));
			list.add(map);
		}
		Object object = JSONArray.toJSON(list);
		return object.toString();
	}
	
	/**
	 * 获取一周的任务剩余数
	 * @param mondayDate 一周的 周一的日期
	 * @param projectId 工程id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getBlocksRemainOfWeek.do", method=RequestMethod.POST)
	public String getBlocksRemainOfWeek(@RequestParam String mondayDate,
										@RequestParam int projectId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date mondayDateD = null;
		try {
			mondayDateD = sdf.parse(mondayDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获取一周中每天剩余任务数
		List<Map<String, String>> list = blockService.getBlocksRemainOfWeek(mondayDateD, projectId);
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 获取工程(项目)中已完成、未完成、延期工程分别数目
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCompleteOrNotCountBlocksOfProject.do", method=RequestMethod.POST)
	public String getCompleteOrNotCountOfProject(@RequestParam int projectId) {
		List<Map<String, String>> list = blockService.getCompleteOrNotCountBlocksOfProject(projectId);
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 获取任务周报(本周完成的任务、本周延迟的任务、本周新增的任务)
	 * @param mondayDate
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getWeeklyBlocks.do", method=RequestMethod.POST)
	public String getWeeklyBlocks(@RequestParam String mondayDate,
								  @RequestParam int projectId) {
		Map<String, List<Block>> map = blockService.getWeeklyBlocks(DateUtil.getDate(mondayDate), projectId);
		return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
	}
	
}
