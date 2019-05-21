package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.RoleMenu;

public interface RoleMenuMapper {
	
	/**
	 * 通过角色id获取角色-菜单关联
	 * @param roleId
	 * @return
	 */
	public List<RoleMenu> getByRoleId(String roleId);
	
	/**
	 * 通过角色id和菜单id获取角色-菜单关联
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public RoleMenu getByRoleIdAndMenuId(String roleId, int menuId);
	
	/**
	 * 添加角色-菜单关联
	 * @param roleMenu
	 * @return
	 */
	public int add(RoleMenu roleMenu);
	
	/**
	 * 删除角色-菜单关联
	 * @param roleMenu
	 * @return
	 */
	public int delete(RoleMenu roleMenu);

}
