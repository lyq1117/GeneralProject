package cn.kgc.realm;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import cn.kgc.pojo.User;
import cn.kgc.service.UserService;
import cn.kgc.service.UserServiceImpl;
import sun.security.krb5.RealmException;

public class MyRealm extends AuthorizingRealm{

	@Resource
	private UserService userService;
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
		System.out.println("授权+++++");
		
		return null;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证++++++");
		UsernamePasswordToken uToken = (UsernamePasswordToken) token;
		User user = new User();
		System.out.println("......" + uToken.getUsername());
		try {
			user = userService.getUserByUsername(uToken.getUsername());
		} catch (Exception e) {
			try {
				throw new RealmException("根据用户名获取用户失败");
			} catch (RealmException e1) {
				e1.printStackTrace();
			}
		}
		//用用户名来生成盐
		ByteSource salt = ByteSource.Util.bytes(uToken.getUsername());
		//验证数据库的用户和前端传来到用户名、密码是否匹配
		SimpleAuthenticationInfo simpleAuthenticationInfo = 
				new SimpleAuthenticationInfo(user,
						user.getPwd(), salt, this.getName());
		return simpleAuthenticationInfo;
	}

}
