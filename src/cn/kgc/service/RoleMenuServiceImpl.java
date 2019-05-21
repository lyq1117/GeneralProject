package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.RoleMenuMapper;
import cn.kgc.pojo.RoleMenu;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
	
	@Resource
	private RoleMenuMapper roleMenuMapper;//角色-菜单映射

	@Override
	public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
		return roleMenuMapper.getByRoleId(roleId);
	}

	@Override
	public boolean hasRoleMenu(String roleId, int menuId) {
		RoleMenu roleMenu = roleMenuMapper.getByRoleIdAndMenuId(roleId, menuId);
		if(roleMenu == null) {
			return false;//没有关联
		}
		return true;//有关联
	}

	@Override
	public int addRoleMenu(String roleId, int menuId) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(roleId);
		roleMenu.setMenuId(menuId);
		int result = roleMenuMapper.add(roleMenu);
		return result;
	}

	@Override
	public int deleteRoleMenu(String roleId, int menuId) {
		RoleMenu roleMenu = new RoleMenu();
		roleMenu.setRoleId(roleId);
		roleMenu.setMenuId(menuId);
		int result = roleMenuMapper.delete(roleMenu);
		return result;
	}

}
