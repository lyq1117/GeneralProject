package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Block;
import cn.kgc.pojo.User;

public interface BlockMapper {
	
	/**
	 * 根据工程id获取工程小块集合
	 * @param projectId
	 * @return
	 */
	public List<Block> getByProjectId(int projectId);
	
	/**
	 * 根据工程小块id获取团队集合
	 * @param blockId
	 * @return
	 */
	public List<User> getByBlockId(int blockId);
	
	/**
	 * 改变工程小块负责人
	 * @param username 新负责人id
	 * @param blockId 工程小块id
	 * @return
	 */
	public int updateLeaderId(String username, int blockId);
	
	/**
	 * 通过id获取工程小块(即任务)的信息
	 * @param blockId
	 * @return
	 */
	public Block getById(int blockId);
	
	/**
	 * 更新工程小块(即任务)
	 * @param block
	 * @return
	 */
	public int update(Block block);
	
	/**
	 * 添加工程小块(任务)
	 * @param block
	 * @return
	 */
	public int add(Block block);
	
	/**
	 * 获取block表id自增到了几
	 * @return
	 */
	public int getIncrement();
	
	/**
	 * 获取所有任务
	 * @return
	 */
	public List<Block> getAll();

}
