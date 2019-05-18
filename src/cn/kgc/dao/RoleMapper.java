package cn.kgc.dao;

import cn.kgc.pojo.Role;

public interface RoleMapper {
	
	/**
	 * 通过id获取角色
	 * @param roleId
	 * @return
	 */
	public Role getById(String roleId);

}
