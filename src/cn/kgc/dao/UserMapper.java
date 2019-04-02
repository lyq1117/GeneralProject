package cn.kgc.dao;


import java.util.Map;

import cn.kgc.pojo.User;

public interface UserMapper {
	
	/**
	 *通过用户名和密码查询用户
	 * @param username
	 * @return
	 */
	public User getByUsername(String username);

}
