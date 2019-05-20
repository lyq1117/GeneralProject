package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.Role;

public interface RoleService {
	
	/**
	 * 通过角色id获取角色
	 * @param roleId
	 * @return
	 */
	public Role getRoleByRoleId(String roleId);
	
	/**
	 * 获取所有角色
	 * @return
	 */
	public List<Role> getAllRoles();

}
