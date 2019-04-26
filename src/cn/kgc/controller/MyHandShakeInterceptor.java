package cn.kgc.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import cn.kgc.pojo.User;

public class MyHandShakeInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest arg0, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
		
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, 
			ServerHttpResponse serverHttpResponse, 
			WebSocketHandler webSocketHandler,
			Map<String, Object> map) throws Exception {
		//获取当前用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		System.out.println(user.getUsername() + "已建立连接");
		
		if(user != null) {
			map.put("uid", user.getUsername());//为服务器创建WebSocketSession做准备
			System.out.println("用户id："+user.getUsername()+" 被加入");
		}else {
			System.out.println("user为空");
			return false;
		}
		
		return true;
	}

}
