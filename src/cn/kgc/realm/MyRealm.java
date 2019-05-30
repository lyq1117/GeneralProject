package cn.kgc.realm;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

import cn.kgc.pojo.Menu;
import cn.kgc.pojo.RoleMenu;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserRole;
import cn.kgc.service.MenuService;
import cn.kgc.service.ProjectService;
import cn.kgc.service.RoleMenuService;
import cn.kgc.service.UserRoleService;
import cn.kgc.service.UserService;
import cn.kgc.service.UserServiceImpl;
import sun.security.krb5.RealmException;

public class MyRealm extends AuthorizingRealm{

	@Resource
	private UserService userService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserRoleService userRoleService;//用户-角色业务对象
	@Resource
	private RoleMenuService roleMenuService;//角色-菜单业务对象
	@Resource
	private MenuService menuService;//菜单业务对象
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
		System.out.println("授权+++++");
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//找出该用户具有的所有角色
		List<UserRole> userRoles = userRoleService.getUserRoleByUserId(user.getUsername());
		for (UserRole userRole : userRoles) {
			//System.out.println("role############ " + userRole.getRoleId());
			//根据角色id获取 角色具有的菜单(包括菜单栏菜单和链接菜单)
			List<RoleMenu> roleMenus = 
					roleMenuService.getRoleMenusByRoleId(userRole.getRoleId());
			for (RoleMenu roleMenu : roleMenus) {
				//根据id获取链接菜单
				Menu menu = menuService.getLinkMenuById(roleMenu.getMenuId());
				if(menu == null)//roleMenu中可能含有不是连接菜单的id，所以menu可能为空
					continue;
				//查到了连接菜单，就把连接菜单的permission加入进permissions
				System.out.println(menu.getPermission() + "-----------ppppppppp");
				authorizationInfo.addStringPermission(menu.getPermission());
			}
		}
		
		return authorizationInfo;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证++++++");
		UsernamePasswordToken uToken = (UsernamePasswordToken) token;
		User user = new User();
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
