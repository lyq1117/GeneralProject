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
	
	/**
	 * 修改用户密码
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @param newPwd2
	 * @return
	 */
	public int changeUserPwd(String userId, String oldPwd, String newPwd);
	
	/**
	 * 修改用户真实姓名、图片地址、电话
	 * @param name
	 * @param icon
	 * @param tel
	 * @return
	 */
	public int changeNameIconTel(String userId, String name, String icon, String tel);
	
	/**
	 * 禁用用户
	 * @param userId
	 * @return
	 */
	public int banUser(String userId);
	
	/**
	 * 启用用户
	 * @param userId
	 * @return
	 */
	public int openUser(String userId);

}
