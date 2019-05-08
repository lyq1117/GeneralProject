package cn.kgc.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.ProjectMapper;
import cn.kgc.dao.UserMapper;
import cn.kgc.dao.UserProjectMapper;
import cn.kgc.pojo.Project;
import cn.kgc.pojo.User;
import cn.kgc.pojo.UserProject;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private UserProjectMapper userProjectMapper;
	@Resource
	private UserMapper userMapper;
	
	@Override
	public List<Project> getProjectsByLeaderId(String leaderId) {
		return projectMapper.getListByLeaderId(leaderId);
	}

	@Override
	public int addProject(Project project) {
		return projectMapper.add(project);
	}

	@Override
	public int getIncrement() {
		return projectMapper.getIncrement();
	}

	@Override
	public Project getProjectById(int projectId) {
		return projectMapper.getById(projectId);
	}

	@Override
	public int updateProject(Project project) {
		return projectMapper.update(project);
	}

	@Override
	public List<User> getMembersOfProject(int projectId) {
		//查询用户-工程关联
		List<UserProject> userProjects = userProjectMapper.getByProjectId(projectId);
		//初始化成员集合
		List<User> members = new ArrayList<>();
		for (UserProject userProject : userProjects) {
			//根据用户名查询用户
			User user = userMapper.getByUsername(userProject.getUserId());
			members.add(user);
		}
		return members;
	}

	@Override
	public List<Project> getAllProjects() {
		return projectMapper.getAll();
	}

}
