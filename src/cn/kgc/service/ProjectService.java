package cn.kgc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;

public interface ProjectService {
	
	/**
	 * 根据项目经理id获取其项目列表
	 * @param leaderId
	 * @return
	 */
	public List<Project> getProjectsByLeaderId(String leaderId);
	
	/**
	 * 添加工程
	 * @param project
	 * @return
	 */
	public int addProject(Project project);
	
	/**
	 * 获取自增主键自增到了几
	 * @return
	 */
	public int getIncrement();
	
	/**
	 * 通过id获取工程信息
	 * @param projectId
	 * @return
	 */
	public Project getProjectById(int projectId);
	
	/**
	 * 更新工程信息
	 * @param project
	 * @return
	 */
	public int updateProject(Project project);
	
	/**
	 * 获取工程成员集合
	 * @param projectId
	 * @return
	 */
	public List<User> getMembersOfProject(int projectId);
	
	/**
	 * 获取所有工程集合
	 * @return
	 */
	public List<Project> getAllProjects();
	
	/**
	 * 获取企业工程概况(包括 待完成工程、延期工程、已完成工程)
	 * @return
	 */
	public Map<String, List<Project>> getProjectStatisticsSurvey();
	
	/**
	 * 获取本周企业每日新增工程和完成工程
	 * @param mondayDate
	 * @return
	 */
	public List<Map<String, String>> getEnterpriseNewAndCompleteProject(Date mondayDate);
	
	/**
	 * 获取企业工程进度统计表格的数据
	 * @return
	 */
	public List<Map<String, String>> getProjectProgressStatistics();

}
