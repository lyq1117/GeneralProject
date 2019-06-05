package cn.kgc.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Service;

import cn.kgc.dao.BlockMapper;
import cn.kgc.pojo.Block;
import cn.kgc.pojo.User;
import cn.kgc.util.DateUtil;

@Service
public class BlockServiceImpl implements BlockService {
	
	@Resource
	private BlockMapper blockMapper;

	@Override
	public List<Block> getBlocksByProjectId(int projectId) {
		return blockMapper.getByProjectId(projectId);
	}

	@Override
	public List<User> getUsersByBlockId(int blockId) {
		return blockMapper.getByBlockId(blockId);
	}

	@Override
	public int updateBlockLeader(String username, int blockId) {
		return blockMapper.updateLeaderId(username, blockId);
	}

	@Override
	public Block getBlockById(int blockId) {
		return blockMapper.getById(blockId);
	}

	@Override
	public int updateBlock(Block block) {
		return blockMapper.update(block);
	}

	@Override
	public int addBlock(Block block) {
		return blockMapper.add(block);
	}

	@Override
	public int getIncrement() {
		return blockMapper.getIncrement();
	}

	@Override
	public List<Map<String, String>> getBlocksRemainOfWeek(Date mondayDate, int projectId) {
		long millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		Date tuesdayDate = new Date(mondayDate.getTime() + millSecondOfDay*1);
		Date wednesdayDate = new Date(mondayDate.getTime() + millSecondOfDay*2);
		Date thursdayDate = new Date(mondayDate.getTime() + millSecondOfDay*3);
		Date fridayDate = new Date(mondayDate.getTime() + millSecondOfDay*4);
		Date saturdayDate = new Date(mondayDate.getTime() + millSecondOfDay*5);
		Date sundayDate = new Date(mondayDate.getTime() + millSecondOfDay*6);
		
		List<Block> blocks = blockMapper.getByProjectId(projectId);
		List<Map<String, String>> list = new ArrayList<>();
		int coun1 = 0;//周一任务数
		int coun2 = 0;//周二任务数
		int coun3 = 0;//周三任务数
		int coun4 = 0;//周四任务数
		int coun5 = 0;//周五任务数
		int coun6 = 0;//周六任务数
		int coun7 = 0;//周天任务数
		for (Block block : blocks) {
			Date createTime = block.getCreateTime();
			Date endTime = getEndDate(createTime, block.getDuration());
			System.out.println("duration:"+block.getDuration()+"--creatTime:" + createTime + "--endTime:" + endTime);
			if(mondayDate.getTime() >= createTime.getTime() &&
					mondayDate.getTime() <= endTime.getTime())
				coun1++;
			if(tuesdayDate.getTime() >= createTime.getTime() &&
					tuesdayDate.getTime() <= endTime.getTime())
				coun2++;
			if(wednesdayDate.getTime() >= createTime.getTime() &&
					wednesdayDate.getTime() <= endTime.getTime())
				coun3++;
			if(thursdayDate.getTime() >= createTime.getTime() &&
					thursdayDate.getTime() <= endTime.getTime())
				coun4++;
			if(fridayDate.getTime() >= createTime.getTime() &&
					fridayDate.getTime() <= endTime.getTime())
				coun5++;
			if(saturdayDate.getTime() >= createTime.getTime() &&
					saturdayDate.getTime() <= endTime.getTime())
				coun6++;
			if(sundayDate.getTime() >= createTime.getTime() &&
					sundayDate.getTime() <= endTime.getTime())
				coun7++;
		}
		Map<String, String> map1 = new HashMap<>();
		map1.put("x", getDateFormat(mondayDate));
		map1.put("count", String.valueOf(coun1));
		list.add(map1);
		Map<String, String> map2 = new HashMap<>();
		map2.put("x", getDateFormat(tuesdayDate));
		map2.put("count", String.valueOf(coun2));
		list.add(map2);
		Map<String, String> map3 = new HashMap<>();
		map3.put("x", getDateFormat(wednesdayDate));
		map3.put("count", String.valueOf(coun3));
		list.add(map3);
		Map<String, String> map4 = new HashMap<>();
		map4.put("x", getDateFormat(thursdayDate));
		map4.put("count", String.valueOf(coun4));
		list.add(map4);
		Map<String, String> map5 = new HashMap<>();
		map5.put("x", getDateFormat(fridayDate));
		map5.put("count", String.valueOf(coun5));
		list.add(map5);
		Map<String, String> map6 = new HashMap<>();
		map6.put("x", getDateFormat(saturdayDate));
		map6.put("count", String.valueOf(coun6));
		list.add(map6);
		Map<String, String> map7 = new HashMap<>();
		map7.put("x", getDateFormat(sundayDate));
		map7.put("count", String.valueOf(coun7));
		list.add(map7);
		return list;
	}
	
	/**
	 * 根据工程小块(任务)的创建时间和工期计算预计结束时间
	 * @param createTime
	 * @param duration
	 * @return
	 */
	public Date getEndDate(Date createTime, int duration){
		long millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		return new Date(createTime.getTime() + millSecondOfDay * duration);
	}
	
	/**
	 * 格式化日期成字符串  yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public String getDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	@Override
	public List<Map<String, String>> getCompleteOrNotCountBlocksOfProject(int projectId) {
		List<Block> blocks = blockMapper.getByProjectId(projectId);//获取项目下的任务集合
		List<Map<String, String>> list = new ArrayList<>();
		int completeCount = 0;
		int delayCount = 0;
		int notCompleteCount = 0;
		for (Block block : blocks) {
			Date endTime = getEndDate(block.getCreateTime(), block.getDuration());
			Date today = DateUtil.getDate(DateUtil.getStr(new Date()));
			
			
			if(today.getTime() <= endTime.getTime() && block.getStatus()==0)
				notCompleteCount++;
			if(today.getTime() > endTime.getTime() && block.getStatus() == 0)
				delayCount++;
			if(block.getStatus()==2)
				completeCount++;
		}
		Map<String, String> completeMap = new HashMap<>();
		completeMap.put("label", "已完成");
		completeMap.put("value", String.valueOf(completeCount));
		list.add(completeMap);
		Map<String, String> delayMap = new HashMap<>();
		delayMap.put("label", "延期");
		delayMap.put("value", String.valueOf(delayCount));
		list.add(delayMap);
		Map<String, String> notCompleteMap = new HashMap<>();
		notCompleteMap.put("label", "未完成");
		notCompleteMap.put("value", String.valueOf(notCompleteCount));
		list.add(notCompleteMap);
		return list;
	}

	@Override
	public Map<String, List<Block>> getWeeklyBlocks(Date mondayDate, int projectId) {
		//一天的毫秒数
		long millSecondOfDay = 24*60*60*1000;
		//本周天的日期
		Date sundayDate = new Date(mondayDate.getTime() + millSecondOfDay*6);
		
		List<Block> delayBlocks = new ArrayList<>();//本周延迟任务集合
		List<Block> completeBlocks = new ArrayList<>();//本周完成任务集合
		List<Block> newBlocks = new ArrayList<>();//本周新增任务集合
		
		List<Block> blocks = blockMapper.getByProjectId(projectId);
		for (Block block : blocks) {
			//计算预计结束时间
			Date endTime = getEndDate(block.getCreateTime(), block.getDuration());
			//状态0(即执行中)且预计结束时间在本周一之前的，都是本周的延期任务
			if(block.getStatus() == 0 && endTime.getTime() < mondayDate.getTime())
				delayBlocks.add(block);
			
			//状态为2(即完成的任务)
			if(block.getStatus() == 2) {
				Date realEndTime = getEndDate(block.getCreateTime(), block.getRealDuration());
				System.out.println("---------realEndTime:" + realEndTime);
				//并且实际结束时间在本周一~周天内的， 都是本周完成的任务
				if(realEndTime.getTime() >= mondayDate.getTime()
						&& realEndTime.getTime() <= sundayDate.getTime())
					completeBlocks.add(block);
			}
			
			//创建时间在本周一~周天内的，都是本周新增任务
			if(block.getCreateTime().getTime() >= mondayDate.getTime()
					&& block.getCreateTime().getTime() <= sundayDate.getTime())
				newBlocks.add(block);
		}
		Map<String, List<Block>> map = new HashMap<>();
		map.put("delayBlocks", delayBlocks);
		map.put("completeBlocks", completeBlocks);
		map.put("newBlocks", newBlocks);
		return map;
	}

	@Override
	public Map<String, List<Block>> getEnterpriseBlockWeekly(Date mondayDate) {
		
		List<Block> blocks = blockMapper.getAll();
		
		//一天的毫秒数
		long millSecondOfDay = 24*60*60*1000;
		//本周天的日期
		Date sundayDate = new Date(mondayDate.getTime() + millSecondOfDay*6);
		
		Date todayDate = new Date();
		
		List<Block> delayBlocks = new ArrayList<>();//本周企业延期任务集合
		List<Block> completeBlocks = new ArrayList<>();//本周企业完成任务结合
		List<Block> newBlocks = new ArrayList<>();//本周企业新增任务集合
		
		for (Block block : blocks) {
			//计算预计结束时间
			Date endTime = getEndDate(block.getCreateTime(), block.getDuration());
			//状态0(即执行中)且预计结束时间在本周一之前的，都是本周的延期任务
			if(block.getStatus() == 0 && endTime.getTime() < mondayDate.getTime()) 
				delayBlocks.add(block);
			//状态2(正常结束、完成)
			if(block.getStatus() == 2) {
				Date realEndTime = getEndDate(block.getCreateTime(), block.getRealDuration());
				if(realEndTime.getTime() >= mondayDate.getTime()
						&& realEndTime.getTime() <= sundayDate.getTime())
					completeBlocks.add(block);
			}
			if(block.getCreateTime().getTime() >= mondayDate.getTime()
					&& block.getCreateTime().getTime() <= sundayDate.getTime())
				newBlocks.add(block);
		}
		
		Map<String, List<Block>> map = new HashMap<>();
		map.put("delayBlocks", delayBlocks);
		map.put("completeBlocks", completeBlocks);
		map.put("newBlocks", newBlocks);
		
		return map;
	}

	@Override
	public List<Block> getAllBlocks() {
		return blockMapper.getAll();
	}

	@Override
	public List<Block> getBlocksByLeaderId(String leaderId) {
		return blockMapper.getByLeaderId(leaderId);
	}

	@Override
	public int ownBlocksCount(String userId) {
		return blockMapper.getByLeaderId(userId).size();
	}

}
