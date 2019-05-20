package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.MenuMapper;
import cn.kgc.pojo.Menu;

@Service
public class MenuServiceImpl implements MenuService {

	@Resource
	private MenuMapper menuMapper;
	
	@Override
	public List<Menu> getRootMenus() {
		return menuMapper.getMenusByParentId(0);//根菜单的父亲id都为0
	}

	@Override
	public Menu getMenuById(int menuId) {
		return menuMapper.getById(menuId);
	}

	@Override
	public List<Menu> getMenusByParentId(int parentId) {
		return menuMapper.getMenusByParentId(parentId);
	}

	@Override
	public Menu getRootMenuById(int menuId) {
		return menuMapper.getRootMenu(menuId);
	}

	@Override
	public Menu getLinkMenuById(int menuId) {
		return menuMapper.getLinkMenu(menuId);
	}

}
