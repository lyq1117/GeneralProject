package cn.kgc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * 获取一周中每天的剩余任务数
	 * @param mondayDate
	 * @param projectId
	 * @return
	 */
	public List<Map<String, String>> getBlocksRemainOfWeek(Date mondayDate, int projectId);
	
	/**
	 * 获取工程(项目)中已完成、未完成、延期工程分别数目
	 * @param projectId
	 * @return
	 */
	public List<Map<String, String>> getCompleteOrNotCountBlocksOfProject(int projectId);
	
	/**
	 * 获取任务周报(本周延期任务、本周完成任务、本周新增任务)
	 * @param mondayDate
	 * @param projectId
	 * @return
	 */
	public Map<String, List<Block>> getWeeklyBlocks(Date mondayDate, int projectId);
	
	/**
	 * 获取企业任务周报(本周延期任务、本周完成任务、本周新增任务)
	 * @param mondayDate
	 * @return
	 */
	public Map<String, List<Block>> getEnterpriseBlockWeekly(Date mondayDate);
	
	/**
	 * 获取所有任务
	 * @return
	 */
	public List<Block> getAllBlocks();
	
	/**
	 * 通过负责人id获取任务集合
	 * @param leaderId
	 * @return
	 */
	public List<Block> getBlocksByLeaderId(String leaderId);
	
	/**
	 * 获取用户管控(即为任务负责人)的任务个数
	 * @param userId
	 * @return
	 */
	public int ownBlocksCount(String userId);
	
	
}
