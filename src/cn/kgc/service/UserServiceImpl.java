package cn.kgc.service;

import java.util.HashMap;
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
	
	

}
