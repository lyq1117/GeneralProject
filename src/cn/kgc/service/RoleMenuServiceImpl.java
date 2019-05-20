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

}
