package cn.kgc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

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

import cn.kgc.pojo.Dept;
import cn.kgc.pojo.Menu;
import cn.kgc.pojo.Role;
import cn.kgc.pojo.RoleMenu;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.DeptService;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleMenuService;
import cn.kgc.service.RoleService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;
import cn.kgc.util.ShiroUtil;

@Controller
@RequestMapping(value="/page/system")
public class SystemController {

	@Resource
	private UserService userService;//用户业务对象
	@Resource
	private RoleService roleService;//角色业务对象
	@Resource
	private UserRoleService userRoleService;//用户角色业务对象
	@Resource
	private RoleMenuService roleMenuService;//角色-菜单业务对象
	@Resource
	private MenuService menuService;//菜单业务对象
	@Resource
	private DeptService deptService;//部门业务对象
	
	/**
	 * 获取用户管理的表格数据
	 * @return
	 */
	@RequiresPermissions("system:getUsersTable")
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
	@RequiresPermissions("system:getAllRoles")
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
	@RequiresPermissions("system:getRolesByUserId")
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
	@RequiresPermissions("system:changeRole")
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
			//如果重新赋予的角色中有普通员工的角色，那么把该用户的部长和总经办部长的职位全撤了
			//如果重新赋予的角色中有普通员工的角色，那么把该用户作为总经办的部长的职位撤销了
			if(roleId.equals("R-103")) {
				deptService.cancelDeptLeaderId(userId);
				deptService.cancelManagerDeptLeaderId(userId);
			}
			//如果重新赋予的角色中有部长的角色，那么把该用户作为总经办的部长的职位撤销了
			if(roleId.equals("R-105")) {
				deptService.cancelManagerDeptLeaderId(userId);
			}
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
	@RequiresPermissions("system:getUserInfo")
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
	@RequiresPermissions("system:editUserinfo")
	@ResponseBody
	@RequestMapping(value="/editUserinfo.do",
					method=RequestMethod.POST)
	public String editUserinfo(@RequestParam boolean isChangePwdFlag,
							   @RequestParam String userId,
							   @RequestParam String oldPwd,
							   @RequestParam String newPwd,
							   @RequestParam String name,
							   @RequestParam String icon,
							   @RequestParam String tel,
							   @RequestParam int deptId) {
		
		//System.out.println(isChangePwdFlag + "-" + oldPwd + "-" + newPwd + "-" + newPwd2 + '-' + name + '-' + icon + "-" + tel);
		//如果有附带改密码操作
		Map<String, String> map = new HashMap<>();
		if(isChangePwdFlag == true) {
			int isChanged = userService.changeUserPwd(userId, oldPwd, newPwd);
			if(isChanged == 0) {//改密码成功
				//修改信息,同时要将此用户作为过那个部门的部长id撤销
				deptService.cancelDeptLeaderId(userId);
				int isChanged2 = userService.changeNameIconTelDeptId(userId, name, icon, tel, deptId);
				map.put("result", "修改成功!");
			}else {
				map.put("result", "修改失败!");
			}
		}else {//没有附带改密码的操作
			//修改信息,同时要将此用户作为过那个部门的部长id撤销
			deptService.cancelDeptLeaderId(userId);
			int isChanged2 =  userService.changeNameIconTelDeptId(userId, name, icon, tel, deptId);
			if(isChanged2 == 1) {
				map.put("result", "修改成功!");
			}
			else {
				map.put("result", "修改失败!");
			}
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 禁用用户
	 * @param userId
	 * @return
	 */
	@RequiresPermissions("system:banUser")
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
	@RequiresPermissions("system:openUser")
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
	
	/**
	 * 获取角色管理表格数据
	 * @return
	 */
	@RequiresPermissions("system:getRoleManageTable")
	@ResponseBody
	@RequestMapping(value="/getRoleManageTable.do",
					method=RequestMethod.GET)
	public String getRoleManageTable(@RequestParam String roleId) {
		//表格数据结果集
		List<Map<String, String>> result = new ArrayList<>();
		
		//获取所有根菜单
		List<Menu> rootMenus = menuService.getRootMenus();
		//遍历所有根菜单
		for (Menu rootMenu : rootMenus) {
			Map<String, String> map = new HashMap<>();
			boolean hasRootMenu = roleMenuService.hasRoleMenu(roleId, rootMenu.getId());
			//获取根菜单下的链接菜单
			List<Menu> linkMenus = menuService.getLinkMenusByParentId(rootMenu.getId());
			String linkMenuStr = "";
			if(hasRootMenu) {//该角色有这个根菜单，就让复选框选中
				map.put("rootMenu", "<input class=\"role_manage_rootCheck\" menuId=\""+rootMenu.getId()+"\" type=\"checkbox\" checked=\"checked\"/>" + rootMenu.getName());
				
				//遍历链接菜单，组成链接菜单数据
				for (Menu linkMenu : linkMenus) {
					boolean hasLinkMenu = roleMenuService.hasRoleMenu(roleId, linkMenu.getId());
					if(hasLinkMenu) {
						linkMenuStr += "<input class=\"role_manage_linkCheck\" menuId=\""+linkMenu.getId()+"\" type=\"checkbox\" checked=\"checked\"/>" + linkMenu.getName() + "<br>";
					}else {
						linkMenuStr += "<input class=\"role_manage_linkCheck\" menuId=\""+linkMenu.getId()+"\" type=\"checkbox\"/>" + linkMenu.getName() + "<br>";
					}
				}
			}
			else {//该角色没有这个根菜单，则下面的链接菜单都编程不可用状态,不可用状态的话 所以事件和menuId都不用传到前端了
				map.put("rootMenu", "<input class=\"role_manage_rootCheck\" menuId=\""+rootMenu.getId()+"\" type=\"checkbox\"/>" + rootMenu.getName());
				
				//遍历链接菜单，组成链接菜单数据
				for (Menu linkMenu : linkMenus) {
					linkMenuStr += "<input type=\"checkbox\" disabled=\"dosabled\"/>" + linkMenu.getName() + "<br>";
				}
			}
			
			map.put("linkMenu", linkMenuStr);
			result.add(map);
		}
		
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 添加角色-菜单关联
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	@RequiresPermissions("system:addRoleMenu")
	@ResponseBody
	@RequestMapping(value="/addRoleMenu.do",
					method=RequestMethod.POST)
	public String addRoleMenu(@RequestParam String roleId,
							  @RequestParam int menuId) {
		int count = roleMenuService.addRoleMenu(roleId, menuId);
		Map<String, Object> result = new HashMap<>();
		if(count == 1)
			result.put("result", true);
		else
			result.put("result", false);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 取消选中根菜单  删除根菜单和角色的关联的同时删除链接菜单和角色的关联
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	@RequiresPermissions("system:cancelRootMenu")
	@ResponseBody
	@RequestMapping(value="/cancelRootMenu.do")
	public String cancelRootMenu(@RequestParam String roleId,
								 @RequestParam int menuId) {
		
		int count = roleMenuService.deleteRoleMenu(roleId, menuId);
		
		//获取根菜单下的连接菜单
		List<Menu> linkMenus = menuService.getLinkMenusByParentId(menuId);
		
		for (Menu linkMenu : linkMenus) {
			//逐个删除根菜单下的链接菜单 和 角色的关联
			roleMenuService.deleteRoleMenu(roleId, linkMenu.getId());
		}
		Map<String, Object> map = new HashMap<>();
		
		if(count != 0)
			map.put("result", true);//取消勾选根菜单成功
		else
			map.put("result", false);//取消勾选根菜单失败
		return JSON.toJSONString(map);
	}
	
	/**
	 * 取消勾选链接菜单
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	@RequiresPermissions("system:cancelLinkMenu")
	@ResponseBody
	@RequestMapping(value="/cancelLinkMenu.do")
	public String cancelLinkMenu(@RequestParam String roleId,
								 @RequestParam int menuId) {
		int count = roleMenuService.deleteRoleMenu(roleId, menuId);
		Map<String, Object> map = new HashMap<>();
		map.put("result", true);
		return JSON.toJSONString(map);
	}
	
	/**
	 * 新增用户
	 * @param userId
	 * @param name
	 * @param pwd
	 * @param pwd2
	 * @param icon
	 * @param tel
	 * @param deptId
	 * @return
	 */
	@RequiresPermissions(value="system:addUser")
	@ResponseBody
	@RequestMapping(value="/addUser.do",
					method=RequestMethod.POST)
	public String addUser(@RequestParam String userId,
						  @RequestParam String name,
						  @RequestParam String pwd,
						  @RequestParam String pwd2,
						  @RequestParam String icon,
						  @RequestParam String tel,
						  @RequestParam int deptId) {
		//md5加密用户输入的密码
		String md5Pwd = ShiroUtil.md5(pwd, userId);
		User user = new User();
		user.setUsername(userId);
		user.setName(name);
		user.setPwd(md5Pwd);
		user.setIcon(icon);
		user.setTel(tel);
		user.setStatus(0);
		Dept dept = deptService.getDeptById(deptId);
		user.setDept(dept);
		int count = userService.addUser(user);
		Map<String, String> map = new HashMap<>();
		if(count == 1) {
			map.put("result", "添加用户成功！");
		}
		else {
			map.put("result", "添加用户失败！");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取部门列表
	 * @return
	 */
	@RequiresPermissions(value="system:getDeptList")
	@ResponseBody
	@RequestMapping(value="/getDeptList.do",
					method=RequestMethod.POST)
	public String getDeptList() {
		List<Dept> depts = deptService.getAllDept();
		return JSONArray.toJSONString(depts);
	}
	
	/**
	 * 表格形式获取部门列表
	 * @return
	 */
	@RequiresPermissions(value="system:getDeptListTable")
	@ResponseBody
	@RequestMapping(value="/getDeptListTable.do")
	public String getDeptListTable() {
		//初始化结果集
		List<Map<String, Object>> result = new ArrayList<>();
		//获取所有部门
		List<Dept> depts = deptService.getAllDept();
		for (Dept dept : depts) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", dept.getId());
			map.put("name", dept.getName());
			map.put("location", dept.getLocation());
			if(dept.getLeader_id() == null || "".equals(dept.getLeader_id())) {
				map.put("leader", "");
			}
			else {
				User leader = userService.getUserByUsername(dept.getLeader_id());
				map.put("leader", leader.getUsername() + "-" + leader.getName());
			}
			if(dept.getStatus() == 0) {
				map.put("status", "<small class=\"label label-success\">可用</small>");
			}
			else {
				map.put("status", "<small class=\"label label-danger\">不可用</small>");
			}
			map.put("option", "<a href=\"javascript:void(0)\" deptId=\""+dept.getId()+"\" class=\"dept_manage_edit\"><i class=\"fa fa-edit\"></i></a>  <a href=\"javascript:void(0)\" deptId=\""+dept.getId()+"\" class=\"dept_manage_open\"><i class=\"fa fa-check\"></i></a>  <a href=\"javascript:void(0)\" deptId=\""+dept.getId()+"\" class=\"dept_manage_close\"><i class=\"fa fa-close\"></i></a>");
			
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 通过id获取部门信息
	 * @param deptId
	 * @return
	 */
	@RequiresPermissions(value="system:getDeptById")
	@ResponseBody
	@RequestMapping(value="/getDeptById.do",
					method=RequestMethod.POST)
	public String getDeptById(@RequestParam int deptId) {
		Dept dept = deptService.getDeptById(deptId);
		return JSON.toJSONString(dept);
	}
	
	/**
	 * 获取部门成员集合
	 * @param deptId
	 * @return
	 */
	@RequiresPermissions(value="system:getDeptMembersOwnDeptLeaderRoleOrManagerRole")
	@ResponseBody
	@RequestMapping(value="/getDeptMembersOwnDeptLeaderRoleOrManagerRole.do",
					method=RequestMethod.POST)
	public String getDeptMembersOwnDeptLeaderRoleOrManagerRole(@RequestParam int deptId) {
		List<User> members = userService.getDeptMembers(deptId);
		List<User> result = new ArrayList<>();//初始化的结果集
		for (User user : members) {
			List<UserRole> userRoles = userRoleService.getUserRoleByUserId(user.getUsername());
			//当用户拥有角色
			if(userRoles.size() != 0 && userRoles != null) {
				for (UserRole userRole : userRoles) {
					//若是部长
					if(userRole.getRoleId().equals("R-105")) {
						result.add(user);
					}
					//若是总经理
					if(userRole.getRoleId().equals("R-101")) {
						result.add(user);
					}
				}
			}
			
		}
		return JSONArray.toJSONString(result);
	}
	
	/**
	 * 修改部门信息
	 * @param deptId
	 * @param deptName
	 * @param location
	 * @param leaderId
	 * @return
	 */
	@RequiresPermissions(value="system:saveDeptInfo")
	@ResponseBody
	@RequestMapping(value="/saveDeptInfo.do",
					method=RequestMethod.POST)
	public String saveDeptInfo(@RequestParam int deptId,
							   @RequestParam String deptName,
							   @RequestParam String location,
							   @RequestParam String leaderId) {
		//封装部门对象
		Dept dept = new Dept();
		dept.setId(deptId);
		dept.setName(deptName);
		dept.setLocation(location);
		dept.setLeader_id(leaderId);
		//保存部门信息
		int count = deptService.saveDeptInfo(dept);
		Map<String, String> map = new HashMap<>();
		if(count == 1) {
			map.put("result", "修改部门信息成功！");
		}
		else {
			map.put("result", "修改部门信息失败！");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 改变部门状态 （用于启用部门和禁用部门）
	 * @param status
	 * @param deptId
	 * @return
	 */
	@RequiresPermissions(value="system:changeDeptStatus")
	@ResponseBody
	@RequestMapping(value="/changeDeptStatus.do",
					method=RequestMethod.POST)
	public String changeDeptStatus(@RequestParam int status,
								   @RequestParam int deptId) {
		int count = deptService.changeDeptStatus(deptId, status);
		Map<String, String> map = new HashMap<>();
		if(count == 1) {
			if(status == 0)
				map.put("result", "启用部门成功！");
			else
				map.put("result", "禁用部门成功");
		}
		else {
			if(status == 0)
				map.put("result", "启用部门失败！");
			else
				map.put("result", "禁用部门失败！");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取所有用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllUsers.do",
					method=RequestMethod.POST)
	public String getAllUsers() {
		List<User> allUsers = userService.getUsers();
		return JSONArray.toJSONString(allUsers);
	}
	
	/**
	 * 新增部门
	 * @param deptName
	 * @param location
	 * @param leaderId
	 * @return
	 */
	@RequiresPermissions(value="system:addDept")
	@ResponseBody
	@RequestMapping(value="/addDept.do",
					method=RequestMethod.POST)
	public String addDept(@RequestParam String deptName,
						  @RequestParam String location) {
		Dept dept = new Dept();
		dept.setLocation(location);
		dept.setName(deptName);
		dept.setStatus(0);
		int count = deptService.addDept(dept);
		Map<String, String> map = new HashMap<>();
		if(count == 1) {
			map.put("result", "新增部门成功！");
		}
		else {
			map.put("result", "新增部门失败！");
		}
		return JSON.toJSONString(map);
	}
	
}
