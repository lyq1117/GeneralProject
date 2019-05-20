package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.UserRoleMapper;
import cn.kgc.pojo.UserRole;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	@Resource
	private UserRoleMapper userRoleMapper;

	@Override
	public List<UserRole> getUserRoleByUserId(String userId) {
		return userRoleMapper.getByUserId(userId);
	}

	@Override
	public UserRole getManagerId() {
		return userRoleMapper.getManagerId();
	}

	@Override
	public int deleteUserRoleByUserId(String userId) {
		return userRoleMapper.deleteByUserId(userId);
	}

	@Override
	public int addUserRole(String userId, String roleId) {
		UserRole userRole = new UserRole(userId, roleId);
		return userRoleMapper.add(userRole);
	}

}
