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
	
	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> getAll();

	/**
	 * 更改用户密码
	 * @param userId
	 * @param pwd
	 * @return
	 */
	public int updatePwd(String userId, String pwd);
	
	/**
	 * 更改用户真实姓名、图片地址、电话
	 * @param userId
	 * @param name
	 * @param icon
	 * @param tel
	 * @return
	 */
	public int updateNameIconTelDeptId(String userId, String name, String icon, String tel, int deptId);
	
	/**
	 * 更改用户状态
	 * @param userId
	 * @param status 0-可用 1-禁用
	 * @return
	 */
	public int updateStatus(String userId, int status);
	
	/**
	 * 添加用户信息
	 * @param user
	 * @return
	 */
	public int add(User user);
	
	/**
	 * 通过部门id获取用户集合
	 * @param deptId
	 * @return
	 */
	public List<User> getByDeptId(int deptId);
	
}
