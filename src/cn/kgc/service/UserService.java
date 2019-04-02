package cn.kgc.service;

import cn.kgc.pojo.User;

public interface UserService {
	
	/**
	 * ͨ���û�����ȡ�û�
	 * @return
	 */
	public User getUserByUsername(String username);

}
