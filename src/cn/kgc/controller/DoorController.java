package cn.kgc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Approval;
import cn.kgc.pojo.Block;
import cn.kgc.pojo.Notice;
import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserBlock;
import cn.kgc.service.ApprovalService;
import cn.kgc.service.BlockService;
import cn.kgc.service.NoticeService;
import cn.kgc.service.ProjectService;
import cn.kgc.service.UserBlockService;

@Controller
@RequestMapping(value="/page/door")
public class DoorController {
	
	@Resource
	private ApprovalService approvalService;//审批业务对象
	@Resource
	private NoticeService noticeService;//公告业务对象
	@Resource
	private ProjectService projectService;//工程业务对象
	@Resource
	private BlockService blockService;//任务业务对象
	@Resource
	private UserBlockService userBlockService;//用户-任务关联业务对象
	
	@RequiresPermissions("door:getUserInfo")
	@ResponseBody
	@RequestMapping(value="/getUserInfo.do",
					method=RequestMethod.POST)
	public String getUserInfo() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		return JSON.toJSONString(user);
	}
	
	/**
	 * 获取我的审批表格数据
	 * @return
	 */
	@RequiresPermissions("door:getMyApprovalTable")
	@ResponseBody
	@RequestMapping(value="/getMyApprovalTable.do")
	public String getMyApprovalTable() {
		//获取当前操作用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//获取当前用户提交的审批集合
		List<Approval> approvals = approvalService.getApprovalsBySubmitUserId(user.getUsername());
		List<Map<String, String>> result = new ArrayList<>();
		int index = 0;
		for (Approval approval : approvals) {
			if(index > 3)
				break;
			Map<String, String> map = new HashMap<>();
			map.put("id", approval.getId());
			map.put("title", approval.getTitle());
			int status = approval.getStatus();
			if(status == 0) {
				map.put("status", "<small class=\"label label-warning\">待审批</small>");
			}
			else if(status == 1) {
				map.put("status", "<small class=\"label label-success\">已同意</small>");
			}else if(status == 2) {
				map.put("status", "<small class=\"label label-danger\">已拒绝</small>");
			}
			result.add(map);
			index++;
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取我的公告集合
	 * @return
	 */
	@RequiresPermissions("door:getMyNotices")
	@ResponseBody
	@RequestMapping(value="/getMyNotices.do",
					method=RequestMethod.POST)
	public String getMyNotices() {
		//获取当前操作用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		//根据发布者获取公告集合
		List<Notice> notices = noticeService.getNoticesBySubmitUserId(user.getUsername());
		
		int size = notices.size()>3 ? 3 : notices.size();
		
		List<Map<String, String>> result = new ArrayList<>();
		for(int i=0; i<size; i++) {
			Map<String, String> map = new HashMap<>();
			map.put("title", notices.get(i).getTitle());
			int status = notices.get(i).getStatus();
			if(status == 1) {
				map.put("status", "<small class=\"label label-danger\">紧急</small>");
			}
			else if(status == 2) {
				map.put("status", "<small class=\"label label-warning\">一般</small>");
			}
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取今日任务表格数据
	 * @return
	 */
	@RequiresPermissions("door:getTodayBlocksTable")
	@ResponseBody
	@RequestMapping(value="/getTodayBlocksTable.do")
	public String getTodayBlocksTable() {
		//获取当前操作用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Map<String, String>> result = new ArrayList<>();//结果集
		int index = 0;
		//获取当前用户参与的任务集合
		List<UserBlock> userBlocks = userBlockService.getUserBlockByUserId(user.getUsername());
		for (UserBlock userBlock : userBlocks) {
			if(index > 3)
				break;
			Map<String, String> map = new HashMap<>();//表格的每一行数据
			Block block = blockService.getBlockById(userBlock.getBlockId());
			//任务描述
			map.put("description", block.getDescription());
			//所属工程
			Project project = projectService.getProjectById(block.getProjectId());
			map.put("projectName", project.getName());
			//任务组长
			User leader = block.getLeader();
			map.put("blockLeader", leader.getUsername() + "-" + leader.getName());
			result.add(map);
			index++;
		}
		return JSONArray.toJSONString(result);
	}
	
	
}
