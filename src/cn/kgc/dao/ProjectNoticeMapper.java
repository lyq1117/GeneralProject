package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.ProjectNotice;

public interface ProjectNoticeMapper {
	
	public List<ProjectNotice> getProjectNotices(int projectId);

}
