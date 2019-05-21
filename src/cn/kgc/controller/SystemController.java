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

import cn.kgc.pojo.Menu;
import cn.kgc.pojo.Role;
import cn.kgc.pojo.RoleMenu;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.MenuService;
import cn.kgc.service.RoleMenuService;
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
	@Resource
	private RoleMenuService roleMenuService;//角色-菜单业务对象
	@Resource
	private MenuService menuService;//菜单业务对象
	
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
	
}
