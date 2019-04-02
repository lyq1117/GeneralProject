package cn.kgc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.kgc.pojo.User;

public class SysInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("进入拦截器.............");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println("拦截器获取到的user---" + user);
		//判断session是否存在已登录用户
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/page/error/401.html");
			return false;//拦截
		}
		return true;//放行
	}

}
