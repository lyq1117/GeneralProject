package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.UserBlock;

public interface UserBlockMapper {
	
	/**
	 * 添加用户-工程小块(任务)关联
	 * @param userId
	 * @param blockId
	 * @return
	 */
	public int add(UserBlock userBlock);
	
	/**
	 * 删除用户-工程小块(任务)关联
	 * @param userBlock
	 * @return
	 */
	public int delete(UserBlock userBlock);
	
	/**
	 * 根据用户id获取用户-任务关联
	 * @param userId
	 * @return
	 */
	public List<UserBlock> getByUserId(String userId);

}
