package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.ProjectNotice;

public interface ProjectNoticeService {
	
	/**
	 * 获取项目公告集合
	 * @param projectId
	 * @return
	 */
	public List<ProjectNotice> getProjectNotices(int projectId);

}
