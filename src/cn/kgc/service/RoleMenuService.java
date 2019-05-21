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
	
	/**
	 * 是否有角色-菜单关联
	 * @param roleId
	 * @param menuId
	 * @return true-有关联  false-没有关联
	 */
	public boolean hasRoleMenu(String roleId, int menuId);
	
	/**
	 * 添加角色-菜单关联
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public int addRoleMenu(String roleId, int menuId);
	
	/**
	 * 删除角色-菜单关联
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public int deleteRoleMenu(String roleId, int menuId);

}
