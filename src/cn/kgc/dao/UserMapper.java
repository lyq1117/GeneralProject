package cn.kgc.dao;


import java.util.List;
import java.util.Map;

import cn.kgc.pojo.User;

public interface UserMapper {
	
	/**
	 *通过用户名和密码查询用户
	 * @param username
	 * @return
	 */
	public User getByUsername(String username);
	
	/**
	 * 查询不是工程成员的用户集合
	 * @param projectId
	 * @return
	 */
	public List<User> getNotInProject(int projectId);
	
	/**
	 * 查询工程小块(即任务)的成员集合
	 * @param blockId
	 * @return
	 */
	public List<User> getOfBlock(int blockId);
	
	/**
	 * 获取不是工程小块成员，是工程成员的用户集合
	 * @param blockId
	 * @param projectId
	 * @return
	 */
	public List<User> getNotInBlockInProject(int blockId, int projectId);

}
