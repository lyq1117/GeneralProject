package cn.kgc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SignService {
	
	/**
	 * 获取企业员工周签到月签到次数统计
	 * @param mondayDate
	 * @param firstDateOfMonth
	 * @param daysOfMonth
	 * @return
	 */
	public List<Map<String, String>> getMemberSignStatistics(Date mondayDate,
															 Date firstDateOfMonth,
															 int daysOfMonth);
	
	/**
	 * 获取企业周出勤人数和缺勤人数
	 * @param mondayDate
	 * @return
	 */
	public Map<String, Object> getWeeklyAttendanceAndNot(Date mondayDate);

}
