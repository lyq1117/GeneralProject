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
		System.out.println("����������.............");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println("��������ȡ����user---" + user);
		//�ж�session�Ƿ�����ѵ�¼�û�
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/page/error/401.html");
			return false;//����
		}
		return true;//����
	}

}
