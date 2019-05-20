package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.UserRole;

public interface UserRoleService {
	
	/**
	 * 通过用户id获取用户-角色关联
	 * @param userId
	 * @return
	 */
	public List<UserRole> getUserRoleByUserId(String userId);
	
	/**
	 * 获取总经理的用户名，因为总经理只有一个，所以返回一个UserRole
	 * @return
	 */
	public UserRole getManagerId();
	
	/**
	 * 根据用户id删除用户-角色关联
	 * @param userId
	 * @return
	 */
	public int deleteUserRoleByUserId(String userId);
	
	/**
	 * 添加用户-角色关联
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public int addUserRole(String userId, String roleId);

}
