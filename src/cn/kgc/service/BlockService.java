package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.Block;
import cn.kgc.pojo.User;

public interface BlockService {
	
	/**
	 * 根据工程id获取工程小块集合
	 * @param projectId
	 * @return
	 */
	public List<Block> getBlocksByProjectId(int projectId);
	
	/**
	 * 根据工程小块id获取团队集合
	 * @param blockId
	 * @return
	 */
	public List<User> getUsersByBlockId(int blockId);
	
	/**
	 * 更新工程小块负责人
	 * @return
	 */
	public int updateBlockLeader(String username, int blockId);
	
	/**
	 * 根据id获取工程小块(即任务)的信息
	 * @param blockId
	 * @return
	 */
	public Block getBlockById(int blockId);
	
	/**
	 * 更新工程小块(即任务)
	 * @param block
	 * @return
	 */
	public int updateBlock(Block block);
	
	/**
	 * 添加工程小块(任务)
	 * @param block
	 * @return
	 */
	public int addBlock(Block block);
	
	/**
	 * 获取block表id自增到了几
	 * @return
	 */
	public int getIncrement();

}
