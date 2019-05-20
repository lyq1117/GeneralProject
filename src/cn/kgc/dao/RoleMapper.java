package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Role;

public interface RoleMapper {
	
	/**
	 * 通过id获取角色
	 * @param roleId
	 * @return
	 */
	public Role getById(String roleId);
	
	/**
	 * 获取所有角色
	 * @return
	 */
	public List<Role> getAll();

}
