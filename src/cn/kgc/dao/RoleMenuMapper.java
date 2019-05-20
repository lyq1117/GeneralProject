package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.RoleMenu;

public interface RoleMenuMapper {
	
	public List<RoleMenu> getByRoleId(String roleId);

}
