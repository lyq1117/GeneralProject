package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.Menu;

public interface MenuService {
	
	/**
	 * 获取根菜单
	 */
	public List<Menu> getRootMenus();
	
	/**
	 * 根据id获取菜单
	 * @param menuId
	 * @return
	 */
	public Menu getMenuById(int menuId);
	
	/**
	 * 根据id获取根菜单
	 * @param menuId
	 * @return
	 */
	public Menu getRootMenuById(int menuId);
	
	/**
	 * 根据父菜单id获取菜单集合
	 * 注：此方法只用于获取根菜单下的子菜单
	 * @param parentId
	 * @return
	 */
	public List<Menu> getMenusByParentId(int parentId);
	
	/**
	 * 根据id获取链接菜单
	 * @param menuId
	 * @return
	 */
	public Menu getLinkMenuById(int menuId);
	
	/**
	 * 根据父菜单id获取连接菜单
	 * @param parentId
	 * @return
	 */
	public List<Menu> getLinkMenusByParentId(int parentId);

}
