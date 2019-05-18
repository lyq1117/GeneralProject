package cn.kgc.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.RoleMapper;
import cn.kgc.pojo.Role;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper roleMapper;
	
	@Override
	public Role getRoleByRoleId(String roleId) {
		return roleMapper.getById(roleId);
	}

}
