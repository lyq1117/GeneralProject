package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.UserRole;

public interface UserRoleMapper {
	
	/**
	 * 通过用户id获取用户-角色id
	 * @param userId
	 * @return
	 */
	public List<UserRole> getByUserId(String userId);
	
	/**
	 * 获取总经理的用户名，因为总经理只有一个，所以这个方法直接返回一个UserRole
	 * @return
	 */
	public UserRole getManagerId();
	
	/**
	 * 根据用户id和角色id删除用户-角色关联
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public int deleteByUserIdAndRoleId(String userId, String roleId);
	
	/**
	 * 根据用户id删除用户-角色关联
	 * @param userId
	 * @return
	 */
	public int deleteByUserId(String userId);
	
	/**
	 * 添加用户-角色关联
	 * @param userRole
	 * @return
	 */
	public int add(UserRole userRole);
}
