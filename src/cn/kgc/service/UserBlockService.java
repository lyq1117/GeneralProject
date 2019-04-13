package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.UserBlock;

public interface UserBlockService {
	
	/**
	 * 添加用户-工程小块(任务)关联
	 * @param userId
	 * @param blockId
	 * @return
	 */
	public int addUserBlock(UserBlock userBlock);
	
	/**
	 * 删除用户-工程小块(任务)关联
	 * @param userBlock
	 * @return
	 */
	public int deleteUserBlock(UserBlock userBlock);
	
	/**
	 * 通过用户id获取用户-任务关联
	 * @param userId
	 * @return
	 */
	public List<UserBlock> getUserBlockByUserId(String userId);

}
