package cn.kgc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Role;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;

@Controller
@RequestMapping(value="/page/system")
public class SystemController {

	@Resource
	private UserService userService;//用户业务对象
	@Resource
	private RoleService roleService;//角色业务对象
	@Resource
	private UserRoleService userRoleService;//用户角色业务对象
	
	
	/**
	 * 获取用户管理的表格数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUsersTable.do")
	public String getUsersTable() {
		//查询所有用户
		List<User> allUsers = userService.getUsers();
		//遍历用户，组成表格数据返回
		List<Map<String, Object>> result = new ArrayList<>();
		for (User user : allUsers) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", user.getUsername());
			map.put("name", user.getName());
			map.put("icon", user.getIcon());
			map.put("tel", user.getTel());
			int status = user.getStatus();
			if(status == 0) {
				map.put("status", "<small class=\"label label-success\">可用</small>");
			}
			else if( status == 1) {
				map.put("status", "<small class=\"label label-danger\">禁用</small>");
			}
			map.put("deptId", user.getDept().getId());
			map.put("deptName", user.getDept().getName());
			map.put("role", "<a href=\"javascript:void(0)\" userId=\""+user.getUsername()+"\" class=\"user_manage_giveRoles\">分配角色</a>");
			map.put("option", "<a href=\"javascript:void(0)\" userId=\""+user.getUsername()+"\" class=\"user_manage_eidt\"><i class=\"fa fa-edit\"></i></a> <a href=\"javascript:void(0)\" userId=\""+user.getUsername()+"\" class=\"user_manage_delete\"><i class=\"fa fa-remove\"></i></a> <a href=\"javascript:void(0)\" userId=\""+user.getUsername()+"\" class=\"user_manage_open\"><i class=\"fa fa-check\"></i></a>");                             
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 获取所有角色
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllRoles.do",
					method=RequestMethod.POST)
	public String getAllRoles() {
		//获取所有角色集合
		List<Role> allRoles = roleService.getAllRoles();
		return JSONArray.toJSONString(allRoles);
	}
	
	/**
	 * 获取用户角色
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRolesByUserId.do")
	public String getRolesByUserId(@RequestParam String userId) {
		List<UserRole> userRoles = userRoleService.getUserRoleByUserId(userId);
		List<Map<String, Object>> result = new ArrayList<>();//结果集
		for (UserRole userRole : userRoles) {
			Map<String, Object> map = new HashMap<>();
			map.put("roleId", userRole.getRoleId());
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 更改用户角色
	 * @param roles
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changeRole.do",
					method=RequestMethod.POST)
	public String changeRole(@RequestParam String[] roles,
							 @RequestParam String userId) {
		//删除用户的所有角色关联
		userRoleService.deleteUserRoleByUserId(userId);
		//重新添加用户的角色关联
		for (String roleId : roles) {
			System.out.println("-------" + roleId + "---------");
			userRoleService.addUserRole(userId, roleId);
		}
		Map<String, String> map = new HashMap<>();
		map.put("result", "分配角色成功!");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 通过用户id获取用户信息
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserInfo.do",
					method=RequestMethod.POST)
	public String getUserInfo(@RequestParam String userId) {
		User user = userService.getUserByUsername(userId);
		return JSON.toJSONString(user);
	}
	
	/**
	 * 编辑修改用户信息
	 * @param isChangePwdFlag
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @param name
	 * @param icon
	 * @param tel
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editUserinfo.do",
					method=RequestMethod.POST)
	public String editUserinfo(@RequestParam boolean isChangePwdFlag,
							   @RequestParam String userId,
							   @RequestParam String oldPwd,
							   @RequestParam String newPwd,
							   @RequestParam String name,
							   @RequestParam String icon,
							   @RequestParam String tel) {
		
		//System.out.println(isChangePwdFlag + "-" + oldPwd + "-" + newPwd + "-" + newPwd2 + '-' + name + '-' + icon + "-" + tel);
		//如果有附带改密码操作
		Map<String, String> map = new HashMap<>();
		if(isChangePwdFlag == true) {
			int isChanged = userService.changeUserPwd(userId, oldPwd, newPwd);
			if(isChanged == 0) {//改密码成功
				//修改信息
				int isChanged2 = userService.changeNameIconTel(userId, name, icon, tel);
				map.put("result", "修改成功!");
			}else {
				map.put("result", "修改失败!");
			}
		}else {//没有附带改密码的操作
			int isChanged2 =  userService.changeNameIconTel(userId, name, icon, tel);
			if(isChanged2 == 1) {
				map.put("result", "修改成功!");
			}
			else {
				map.put("result", "修改失败3!");
			}
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 禁用用户
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/banUser.do",
					method=RequestMethod.POST)
	public String banUser(@RequestParam String userId) {
		int isBaned = userService.banUser(userId);
		Map<String, String> map = new HashMap<>();
		if(isBaned == 1) {
			map.put("result", "禁用成功!");
		}
		else {
			map.put("result", "禁用失败!");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 启用用户
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/openUser.do",
					method=RequestMethod.POST)
	public String openUser(@RequestParam String userId) {
		int isBaned = userService.openUser(userId);
		Map<String, String> map = new HashMap<>();
		if(isBaned == 1) {
			map.put("result", "启用成功!");
		}
		else {
			map.put("result", "启用失败!");
		}
		return JSON.toJSONString(map);
	}
	
}
