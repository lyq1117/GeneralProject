package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.RoleMenu;

public interface RoleMenuService {
	
	/**
	 * 根据角色id获取角色-菜单集合
	 * @param roleId
	 * @return
	 */
	public List<RoleMenu> getRoleMenusByRoleId(String roleId);

}
