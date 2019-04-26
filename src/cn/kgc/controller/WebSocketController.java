package cn.kgc.controller;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping(value="/page")
public class WebSocketController {

	@ResponseBody
	@RequestMapping(value="/message/getSocketPath.do", method=RequestMethod.POST)
	public String getSocketPath(HttpServletRequest request) {
		String path = request.getContextPath();
		String socketPath = request.getServerName() + ":" + request.getServerPort() + path + "/";
		Map<String, String> map = new HashMap<>();
		map.put("socketPath", socketPath);
		return JSON.toJSONString(map);
	}
	
}
