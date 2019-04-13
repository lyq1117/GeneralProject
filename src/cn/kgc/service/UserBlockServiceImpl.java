package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.UserBlockMapper;
import cn.kgc.pojo.UserBlock;


@Service
public class UserBlockServiceImpl implements UserBlockService {
	
	@Resource
	private UserBlockMapper userBlockMapper;

	@Override
	public int addUserBlock(UserBlock userBlock) {
		return userBlockMapper.add(userBlock);
	}

	@Override
	public int deleteUserBlock(UserBlock userBlock) {
		return userBlockMapper.delete(userBlock);
	}

	@Override
	public List<UserBlock> getUserBlockByUserId(String userId) {
		return userBlockMapper.getByUserId(userId);
	}

}
