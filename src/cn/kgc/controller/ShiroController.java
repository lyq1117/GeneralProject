package cn.kgc.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/shiro")
public class ShiroController {

	@ResponseBody
	@RequestMapping(value="/unauthorized.do")
	public String unauthorized() {
		System.out.println("---------------------------------------------------------------------------------------------");
		return null;
	}
	
}
