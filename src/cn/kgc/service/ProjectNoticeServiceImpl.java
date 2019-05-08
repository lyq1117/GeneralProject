package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.ProjectNoticeMapper;
import cn.kgc.pojo.ProjectNotice;

@Service
public class ProjectNoticeServiceImpl implements ProjectNoticeService {
	
	@Resource
	private ProjectNoticeMapper projectNoticeMapper;

	@Override
	public List<ProjectNotice> getProjectNotices(int projectId) {
		return projectNoticeMapper.getProjectNotices(projectId);
	}

}
