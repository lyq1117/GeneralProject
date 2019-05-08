package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;

public interface ProjectMapper {
	
	/**
	 * 根据项目经理id获取其项目列表
	 * @param leaderId
	 * @return
	 */
	public List<Project> getListByLeaderId(String leaderId);
	
	/**
	 * 添加工程
	 * @param project
	 * @return
	 */
	public int add(Project project);
	
	/**
	 * 获取自增主键已经增长到了几
	 * @return
	 */
	public int getIncrement();
	
	/**
	 * 根据id获取工程信息
	 * @param projectId
	 * @return
	 */
	public Project getById(int projectId);
	
	/**
	 * 更新工程信息
	 * @param project
	 * @return
	 */
	public int update(Project project);
	
	/**
	 * 获取所有工程集合
	 * @return
	 */
	public List<Project> getAll();
	
}
