package cn.kgc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.User;
import cn.kgc.service.UserService;

@Controller
@RequestMapping(value="/page")
public class UserController {
	
	@Resource
	private UserService userService;
	
	/**
	 * 获取所有用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserList.do")
	public String getUserList() {
		//获取所有用户集合
		List<User> users = userService.getUsers();
		Subject subject = SecurityUtils.getSubject();
		User principal = (User) subject.getPrincipal();
		List<User> users2 = new ArrayList<>();
		for (User user : users) {
			if(!user.getUsername().equals(principal.getUsername()))
				users2.add(user);
		}
		return JSONArray.toJSONString(users2);
	}

}
