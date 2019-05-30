package cn.kgc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.UserMapper;
import cn.kgc.pojo.User;
import cn.kgc.util.ShiroUtil;

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

	@Override
	public List<User> getUsers() {
		return userMapper.getAll();
	}

	@Override
	public int changeUserPwd(String userId, String oldPwd, String newPwd) {
		User user = userMapper.getByUsername(userId);
		String oldMd5 = ShiroUtil.md5(oldPwd, userId);
		if(oldMd5.equals(user.getPwd())) {//如果旧密码输入正确
			int count = userMapper.updatePwd(userId, ShiroUtil.md5(newPwd, userId));
			if(count == 1) 
				return 0;
		}
		return 1;
	}

	@Override
	public int changeNameIconTel(String userId, String name, String icon, String tel) {
		return userMapper.updateNameIconTel(userId, name, icon, tel);
	}

	@Override
	public int banUser(String userId) {
		return userMapper.updateStatus(userId, 1);//禁用用户  即状态改为1
	}

	@Override
	public int openUser(String userId) {
		return userMapper.updateStatus(userId, 0);//启用用户  即状态改为0
	}

	@Override
	public int addUser(User user) {
		return userMapper.add(user);
	}
	
	

}
