package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Menu;

public interface MenuMapper {
	
	/**
	 * 根据父菜单id获取菜单集合
	 * 注：此方法只用于获取根菜单下的子菜单
	 * @return
	 */
	public List<Menu> getMenusByParentId(int parentId);
	
	/**
	 * 根据id获取菜单
	 * @param menuId
	 * @return
	 */
	public Menu getById(int menuId);
	
	/**
	 * 根据id获取根菜单
	 * @param menuId
	 * @return
	 */
	public Menu getRootMenu(int menuId);
	
	/**
	 * 通过id获取链接菜单
	 * @param menuId
	 * @return
	 */
	public Menu getLinkMenu(int menuId);
	
	/**
	 * 根据父亲id获取链接菜单
	 * @param parentId
	 * @return
	 */
	public List<Menu> getLinkMenusByParentId(int parentId);

}
