package cn.kgc.dao;

import java.util.Date;

import cn.kgc.pojo.Sign;

public interface SignMapper {
	
	/**
	 * 根据用户id和签到日期获取签到信息
	 * @param userId
	 * @param signDate
	 * @return
	 */
	public Sign getByUserIdAndSignDate(String userId, Date signDate);

	/**
	 * 获取某天签到人数
	 * @param signDate
	 * @return
	 */
	public int getSignAccountOfDate(Date signDate);
	
	/**
	 * 添加签到信息
	 * @param sign
	 * @return
	 */
	public int add(Sign sign);
	
}
