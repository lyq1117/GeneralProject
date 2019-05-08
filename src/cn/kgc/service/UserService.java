package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.User;

public interface UserService {
	
	/**
	 * 根据用户名获取用户
	 * @return
	 */
	public User getUserByUsername(String username);
	
	/**
	 * 获取不是工程成员的用户集合
	 * @param projectId
	 * @return
	 */
	public List<User> getUsersNotInProject(int projectId);
	
	/**
	 * 获取工程小块(即任务)成员集合
	 * @param blockId
	 * @return
	 */
	public List<User> getMembersOfBlock(int blockId);
	
	/**
	 * 获取不是工程小块成员，是工程成员的用户集合
	 * @param blockId
	 * @param projectId
	 * @return
	 */
	public List<User> getMembersNotInBlockInProject(int blockId, int projectId);
	
	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> getUsers();

}
