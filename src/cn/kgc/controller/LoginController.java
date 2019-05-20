package cn.kgc.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Block;
import cn.kgc.pojo.Menu;
import cn.kgc.pojo.Project;
import cn.kgc.pojo.RoleMenu;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.BlockService;
import cn.kgc.service.MenuService;
import cn.kgc.service.ProjectService;
import cn.kgc.service.RoleMenuService;
import cn.kgc.service.RoleService;
import cn.kgc.service.SignService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;

@Controller
@RequestMapping("/page")
public class LoginController {
	
	@Resource
	private UserService userService;
	@Resource
	private ProjectService projectService;//工程业务对象
	@Resource
	private BlockService blockService;//任务业务对象
	@Resource
	private SignService signService;//签到业务对象
	@Resource
	private MenuService menuService;//菜单业务对象
	@Resource
	private RoleService roleService;//角色业务对象
	@Resource
	private UserRoleService userRoleService;//用户-角色业务对象
	@Resource
	private RoleMenuService roleMenuService;//角色-菜单业务对象
	
	
	@ResponseBody
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public String doLogin(@RequestParam String username,
						  @RequestParam String pwd,
						  HttpServletRequest request,
						  HttpSession session) {
		System.out.println("login controller");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
		try {
			subject.login(token);
		} catch (Exception e) {
			System.out.println("用户名或者密码错误");
			Map<String, String> map = new HashMap<>();
			map.put("result", "false");
			Object object = JSON.toJSON(map);
			return object.toString();
		}
		Map<String, String> map = new HashMap<>();
		map.put("result", "true");
		Object object = JSON.toJSON(map);
		User user = (User)subject.getPrincipal();
		return object.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getLoginUser.do")
	public String getLoginUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		Object object = JSON.toJSON(user);
		return object.toString();
	}
	
	/**
	 * 获取主页题头的数据（工程数、员工数、任务数）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getIndexTitleData.do",
					method=RequestMethod.POST)
	public String getIndexTitleData() {
		
		List<Project> projects = projectService.getAllProjects();
		List<Block> blocks = blockService.getAllBlocks();
		List<User> users = userService.getUsers();
		Map<String, Object> map = new HashMap<>();
		map.put("projectCount", projects.size());
		map.put("blockCount", blocks.size());
		map.put("userCount", users.size());
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 验证用户是否签过到
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/isUserSigned.do",
					method=RequestMethod.POST)
	public String isUserSigned() {
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		System.out.println("----" + user.getUsername());
		boolean isSign = signService.isSigned(user.getUsername(), new Date());
		System.out.println("----" + isSign);
		Map<String, Object> map = new HashMap<>();
		map.put("result", isSign);
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 用户签到
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/userSign.do",
					method=RequestMethod.POST)
	public String userSign() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		int count = signService.userSign(user);
		Map<String, String> map = new HashMap<>();
		if(count == 1) {
			map.put("result", "true");
		}
		else {
			map.put("result", "false");
		}
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取主页左边菜单数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLeftMenu.do",
					method=RequestMethod.POST)
	public String getLeftMenu() {
		//获取当前用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//获取当前用户的所有角色
		List<UserRole> userRoles = userRoleService.getUserRoleByUserId(user.getUsername());
		
		Set<Integer> set = new HashSet<>();
		for (UserRole userRole : userRoles) {
			List<RoleMenu> roleMenus = roleMenuService.getRoleMenusByRoleId(userRole.getRoleId());//通过角色id获取菜单集合
			for (RoleMenu roleMenu : roleMenus) {
				set.add(roleMenu.getMenuId());
			}
		}
		//初始化结果集
		List<Map<String, Object>> result = new ArrayList<>();
		//遍历根菜单id
		System.out.println("set...size>>>>>>>" + set.size());
		for (Integer menuId : set) {
			Map<String, Object> map = new HashMap<>();
			//根菜单
			Menu rootMenu = menuService.getRootMenuById(menuId);
			if(rootMenu == null)
				continue;//如果不是根菜单，那么rootMenu就会是null的，就跳过此次循环
			map.put("root", rootMenu);
			//根据根菜单获取其子菜单集合
			List<Menu> children = menuService.getMenusByParentId(rootMenu.getId());
			map.put("children", children);
			result.add(map);
		}
		return JSONArray.toJSONString(result);
	}
	
	@Test
	public void test() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(1);
		list.add(4);
		
		Set<Integer> set = new HashSet<>();
		set.add(1);
		set.add(2);
		set.add(1);
		set.add(4);
		
		for (Integer integer : set) {
			System.out.println(integer);
		}
		
	}

}
