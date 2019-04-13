package cn.kgc.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.UserProjectMapper;
import cn.kgc.pojo.UserProject;

@Service
public class UserProjectServiceImpl implements UserProjectService {
	
	@Resource
	private UserProjectMapper userProjectMapper;

	@Override
	public int add(UserProject userProject) {
		return userProjectMapper.add(userProject);
	}

	@Override
	public int delete(UserProject userProject) {
		return userProjectMapper.delete(userProject);
	}

}
