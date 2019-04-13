package cn.kgc.service;

import cn.kgc.pojo.UserProject;

public interface UserProjectService {
	
	/**
	 * 添加用户-工程关联
	 * @param userProject
	 * @return
	 */
	public int add(UserProject userProject);
	
	/**
	 * 删除用户-工程关联
	 * @param userProject
	 * @return
	 */
	public int delete(UserProject userProject);

}
