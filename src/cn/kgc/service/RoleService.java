package cn.kgc.service;

import cn.kgc.pojo.Role;

public interface RoleService {
	
	/**
	 * 通过角色id获取角色
	 * @param roleId
	 * @return
	 */
	public Role getRoleByRoleId(String roleId);

}
