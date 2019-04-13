package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.UserProject;

public interface UserProjectMapper {
	
	/**
	 * 添加用户-工程关联
	 * @return
	 */
	public int add(UserProject userProject);
	
	/**
	 * 根据工程id获取关联集合
	 * @param projectId
	 * @return
	 */
	public List<UserProject> getByProjectId(int projectId);
	
	/**
	 * 删除用户-工程关联
	 * @param userProject
	 * @return
	 */
	public int delete(UserProject userProject);

}
