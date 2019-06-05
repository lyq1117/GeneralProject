package cn.kgc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Approval;
import cn.kgc.pojo.Dept;
import cn.kgc.pojo.Notice;
import cn.kgc.pojo.Role;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.ApprovalService;
import cn.kgc.service.DeptService;
import cn.kgc.service.NoticeService;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;
import cn.kgc.util.DateUtil;
import net.sf.ehcache.search.aggregator.Count;

@Controller
@RequestMapping(value="/page/application")
public class ApplicationController {
	
	@Resource
	private ApprovalService approvalService;//审批业务对象
	@Resource
	private DeptService deptService;//部门业务对象
	@Resource
	private UserService userService;//用户业务对象
	@Resource
	private UserRoleService userRoleService;//用户-角色关联业务对象
	@Resource
	private RoleService roleService;//角色业务对象
	@Resource
	private NoticeService noticeService;//公告业务对象
	
	/**
	 * 获取人事审批的模板
	 * @param request
	 * @return
	 */
	@RequiresPermissions("application:getRenshiApprovalModal")
	@ResponseBody
	@RequestMapping(value="/getRenshiApprovalModal.do", method=RequestMethod.POST)
	public String getRenshiApprovalModal(HttpServletRequest request) {
		
		String modalPath = request.getServletContext().getRealPath("") + "approval_modal" + 
						File.separator + "renshi.xml";
		List<Map<String, Object>> result = approvalService.getApprovalModal(modalPath);
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取财务审批的模板
	 * @param request
	 * @return
	 */
	@RequiresPermissions("application:getCaiwuApprovalModal")
	@ResponseBody
	@RequestMapping(value="/getCaiwuApprovalModal.do", method=RequestMethod.POST)
	public String getCaiwuApprovalModal(HttpServletRequest request) {
		
		String modalPath = request.getServletContext().getRealPath("") + "approval_modal" + 
				File.separator + "caiwu.xml";
		List<Map<String, Object>> result = approvalService.getApprovalModal(modalPath);
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取行政审批模板
	 * @param request
	 * @return
	 */
	@RequiresPermissions("application:getXingzhengApprovalModal")
	@ResponseBody
	@RequestMapping(value="/getXingzhengApprovalModal.do", method=RequestMethod.POST)
	public String getXingzhengApprovalModal(HttpServletRequest request) {
		String modalPath = request.getServletContext().getRealPath("") + "approval_modal" + 
				File.separator + "xingzheng.xml";
		List<Map<String, Object>> result = approvalService.getApprovalModal(modalPath);
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 提交审批信息
	 * @param title
	 * @param content
	 * @param type
	 * @return
	 */
	@RequiresPermissions("application:addApproval")
	@ResponseBody
	@RequestMapping(value="/addApproval.do",
					method=RequestMethod.POST)
	public String addApproval(@RequestParam String title,
							  @RequestParam String content,
							  @RequestParam String type) {
		//获取当前操作的用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		//查询当前用户所在部门
		Dept dept = deptService.getDeptById(user.getDept().getId());
		
		//查询所在部门的领导人
		User leader = userService.getUserByUsername(dept.getLeader_id());
		
		Approval approval = new Approval();
		Calendar c = Calendar.getInstance();
		String id = String.valueOf(c.get(Calendar.YEAR)) + 
					String.valueOf(c.get(Calendar.MONTH)+1) + 
					String.valueOf(c.get(Calendar.DATE)) + 
					String.valueOf(c.getTime().getTime());
		approval.setId(id);
		approval.setType(Integer.parseInt(type));//审批类型 0-人事审批 1-财务审批 2-行政审批
		approval.setTitle(title);
		approval.setContent(content);
		approval.setSubmitUserId(user.getUsername());//提交审批的人
		approval.setSubmitDate(new Date());//提交审批日期
		//获取当前用户的角色
		List<UserRole> userRoles = userRoleService.getUserRoleByUserId(user.getUsername());
		for (UserRole userRole : userRoles) {
			Role role = roleService.getRoleByRoleId(userRole.getRoleId());
			//因为提交审批操作只有员工和部长可以做，所以这里这需要判断两个角色
			if(role.getName().equals("员工")) {
				//本人是员工，就把审批人设置成部长
				approval.setApprovalUserId(leader.getUsername());//审批人
			}
			else if(role.getName().equals("部长")) {
				//本人是部长，就把审批人设置成总经理
				approval.setApprovalUserId(userRoleService.getManagerId().getUserId());
			}
		}
		approval.setStatus(0);//刚提交的审批。状态设置为0-带审批
		
		Map<String, String> map = new HashMap<>();
		try {
			approvalService.addApproval(approval);
			map.put("result", "提交审批成功!");
			return JSON.toJSONString(map);
		} catch (Exception e) {
		}
		map.put("result", "提交审批失败!");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取我审批的表格数据
	 * @return
	 */
	@RequiresPermissions("application:getWspdTable")
	@ResponseBody
	@RequestMapping(value="/getWspdTable.do")
	public String getWspdTable() {
		//获取当前用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Approval> approvals = approvalService.getApprovalsByApprovalUserId(user.getUsername());
		
		List<Map<String, String>> result = new ArrayList<>();//表格结果集
		
		for (Approval approval : approvals) {
			
			Map<String, String> map = new HashMap<>();
			map.put("id", approval.getId());
			String typeStr = "";
			if(approval.getType() == 0)
				typeStr = "人事审批";
			else if(approval.getType() == 1)
				typeStr = "财务审批";
			else if(approval.getType() == 2)
				typeStr = "行政审批";
			map.put("type", typeStr);
			User submitUser = userService.getUserByUsername(approval.getSubmitUserId());
			map.put("submitUser", submitUser.getUsername() + "-" + submitUser.getName());
			map.put("submitDate", DateUtil.getStr(approval.getSubmitDate()));
			String statusStr = "";
			if(approval.getStatus() == 0)
				statusStr = "<small class=\"label label-warning\">待审批</small>";
			else if(approval.getStatus() == 1)
				statusStr = "<small class=\"label label-success\">已同意</small>";
			else if(approval.getStatus() ==2)
				statusStr = "<small class=\"label label-danger\">已拒绝</small>";
			map.put("status", statusStr);
			result.add(map);
		}
		
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取审批详细信息
	 * @param approvalId
	 * @return
	 */
	@RequiresPermissions("application:getApprovalById")
	@ResponseBody
	@RequestMapping(value="/getApprovalById.do",
					method=RequestMethod.POST)
	public String getApprovalById(@RequestParam String approvalId) {
		
		Approval approval = approvalService.getApprovalById(approvalId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("id", approval.getId());
		result.put("title", approval.getTitle());
		result.put("status", approval.getStatus());
		User submitUser = userService.getUserByUsername(approval.getSubmitUserId());
		result.put("submitUser", submitUser);
		result.put("content", approval.getContent());
		result.put("submitDate", DateUtil.getStr(approval.getSubmitDate()));
		
		return JSON.toJSONString(result);
	}
	
	/**
	 * 改变审批
	 * @param approvalId
	 * @return
	 */
	@RequiresPermissions("application:changeApprovalStatus")
	@ResponseBody
	@RequestMapping(value="/changeApprovalStatus.do",
					method=RequestMethod.POST)
	public String changeApprovalStatus(@RequestParam String approvalId,
									   @RequestParam String status) {
		
		Date todayDate = new Date();//当天日期
		Map<String, String> map = new HashMap<>();
		if(Integer.parseInt(status) == 1) {//同意审批
			int count = approvalService.confirmApproval(approvalId, todayDate);
			if(count == 1) {
				map.put("result", "审批成功!");
			}
			else
				map.put("result", "审批失败!");
		}
		else if(Integer.parseInt(status) == 2) {//拒绝审批
			int count = approvalService.rejectApproval(approvalId, todayDate);
			if(count == 1) {
				map.put("result", "审批成功!");
			}
			else
				map.put("result", "审批失败!");
		}
		return JSON.toJSONString(map);
		
	}
	
	/**
	 * 获取我申请的表格数据
	 * @return
	 */
	@RequiresPermissions("application:getWsqdTable")
	@ResponseBody
	@RequestMapping(value="/getWsqdTable.do")
	public String getWsqdTable() {
		//获取当前用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Approval> approvals = approvalService.getApprovalsBySubmitUserId(user.getUsername());
		
		List<Map<String, String>> result = new ArrayList<>();//表格结果集
		
		for (Approval approval : approvals) {
			
			Map<String, String> map = new HashMap<>();
			map.put("id", approval.getId());
			String typeStr = "";
			if(approval.getType() == 0)
				typeStr = "人事审批";
			else if(approval.getType() == 1)
				typeStr = "财务审批";
			else if(approval.getType() == 2)
				typeStr = "行政审批";
			map.put("type", typeStr);
			User submitUser = userService.getUserByUsername(approval.getSubmitUserId());
			map.put("submitUser", submitUser.getUsername() + "-" + submitUser.getName());
			map.put("submitDate", DateUtil.getStr(approval.getSubmitDate()));
			String statusStr = "";
			if(approval.getStatus() == 0)
				statusStr = "<small class=\"label label-warning\">待审批</small>";
			else if(approval.getStatus() == 1)
				statusStr = "<small class=\"label label-success\">已同意</small>";
			else if(approval.getStatus() ==2)
				statusStr = "<small class=\"label label-danger\">已拒绝</small>";
			map.put("status", statusStr);
			result.add(map);
		}
		
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取企业公告的所有公告的表格数据
	 * @return
	 */
	@RequiresPermissions(value="application:getAllNoticeTable")
	@ResponseBody
	@RequestMapping(value="/getAllNoticeTable.do")
	public String getAllNoticeTable() {
		List<Notice> allNotice = noticeService.getAllNotcie();
		
		List<Map<String, Object>> result = new ArrayList<>();//初始化最后表格结果集
		for (Notice notice : allNotice) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", notice.getId());
			map.put("title", notice.getTitle());
			User submitUser = userService.getUserByUsername(notice.getSubmitUserId());
			map.put("submitUser", submitUser.getUsername() + "-" + submitUser.getName());
			map.put("submitDate", DateUtil.getStr(notice.getSubmitDate()));
			if(notice.getStatus() == 1)
				map.put("status", "<small class=\"label label-danger\">紧急</small>");
			else if(notice.getStatus() == 2)
				map.put("status", "<small class=\"label label-warning\">一般</small>");
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 发布公告
	 * @param title
	 * @param content
	 * @param status
	 * @return
	 */
	@RequiresPermissions(value="application:submitNotice")
	@ResponseBody
	@RequestMapping(value="/submitNotice.do",
					method=RequestMethod.POST)
	public String submitNotice(@RequestParam String title,
							   @RequestParam String content,
							   @RequestParam int status) {
		//获取当前操作用户
		Subject subject = SecurityUtils.getSubject();
		User submitUser = (User) subject.getPrincipal();
		
		Notice notice = new Notice();
		notice.setTitle(title);
		notice.setContent(content);
		notice.setSubmitDate(new Date());
		notice.setSubmitUserId(submitUser.getUsername());
		notice.setStatus(status);
		//添加公告信息
		int count = noticeService.addNotice(notice);
		Map<String, String> result = new HashMap<>();
		if(count == 1) {
			result.put("result", "发布成功!");
		}
		else {
			result.put("result", "发布失败!");
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 通过id获取公告
	 * @param id
	 * @return
	 */
	@RequiresPermissions(value="application:getNoticeById")
	@ResponseBody
	@RequestMapping(value="/getNoticeById.do",
					method=RequestMethod.POST)
	public String getNoticeById(@RequestParam int id) {
		Notice notice = noticeService.getNoticeById(id);
		return JSON.toJSONString(notice);
	}
	
	/**
	 * 获取我发布的公告表格数据
	 * @return
	 */
	@RequiresPermissions(value="application:getMyNoticeTable")
	@ResponseBody
	@RequestMapping(value="/getMyNoticeTable.do")
	public String getMyNoticeTable() {
		//获取当前操作的用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//根据发布者id查询公告集合
		List<Notice> notices = noticeService.getNoticesBySubmitUserId(user.getUsername());
		
		List<Map<String, Object>> result = new ArrayList<>();//初始化最后表格结果集
		for (Notice notice : notices) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", notice.getId());
			map.put("title", notice.getTitle());
			User submitUser = userService.getUserByUsername(notice.getSubmitUserId());
			map.put("submitUser", submitUser.getUsername() + "-" + submitUser.getName());
			map.put("submitDate", DateUtil.getStr(notice.getSubmitDate()));
			if(notice.getStatus() == 1)
				map.put("status", "<small class=\"label label-danger\">紧急</small>");
			else if(notice.getStatus() == 2)
				map.put("status", "<small class=\"label label-warning\">一般</small>");
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}

}
