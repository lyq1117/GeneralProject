package cn.kgc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.kgc.pojo.User;
import cn.kgc.service.UserService;

@Controller
@RequestMapping("/page")
public class LoginController {
	
	@Resource
	private UserService userService;
	
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

}
