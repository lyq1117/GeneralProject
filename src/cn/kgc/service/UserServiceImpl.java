package cn.kgc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.UserMapper;
import cn.kgc.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	@Override
	public User getUserByUsername(String username) {
		return userMapper.getByUsername(username);
	}

	@Override
	public List<User> getUsersNotInProject(int projectId) {
		return userMapper.getNotInProject(projectId);
	}

	@Override
	public List<User> getMembersOfBlock(int blockId) {
		return userMapper.getOfBlock(blockId);
	}

	@Override
	public List<User> getMembersNotInBlockInProject(int blockId, int projectId) {
		return userMapper.getNotInBlockInProject(blockId, projectId);
	}
	
	

}
