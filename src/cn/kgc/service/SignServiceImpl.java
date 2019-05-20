package cn.kgc.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Service;

import cn.kgc.dao.SignMapper;
import cn.kgc.dao.UserMapper;
import cn.kgc.pojo.Sign;
import cn.kgc.pojo.User;
import cn.kgc.util.DateUtil;

@Service
public class SignServiceImpl implements SignService {
	
	@Resource
	private UserMapper userMapper;
	@Resource
	private SignMapper signMapper;

	@Override
	public List<Map<String, String>> getMemberSignStatistics(Date mondayDate, Date firstDateOfMonth, int daysOfMonth) {
		
		List<User> allUsers = userMapper.getAll();
		//一天的毫秒数
		long millSecondOfDay = 24*60*60*1000;
		//本周天的日期
		Date sundayDate = new Date(mondayDate.getTime() + millSecondOfDay*6);
		//准备的结果集合
		List<Map<String, String>> result = new ArrayList<>();
		for (User user : allUsers) {
			
			int weekSignCount = 0;//周签到次数
			for(int i=0; i<=6; i++) {
				Sign sign = signMapper.getByUserIdAndSignDate(user.getUsername(), new Date(mondayDate.getTime() + millSecondOfDay*i));
				if(sign != null) {
					weekSignCount++;
				}
			}
			
			int monthSignCount = 0;//月签到次数
			for(int i=0; i<=daysOfMonth-1; i++) {
				Sign sign = signMapper.getByUserIdAndSignDate(user.getUsername(), new Date(firstDateOfMonth.getTime() + millSecondOfDay*i));
				if(sign != null) {
					monthSignCount++;
				}
			}
			
			Map<String, String> map = new HashMap<>();
			map.put("name", user.getUsername() + "-" + user.getName());
			map.put("weekCount", String.valueOf(weekSignCount));
			map.put("monthCount", String.valueOf(monthSignCount));
			
			result.add(map);
		}
		return result;
	}

	@Override
	public Map<String, Object> getWeeklyAttendanceAndNot(Date mondayDate) {
		//一天的毫秒数
		long millSecondOfDay = 24*60*60*1000;
		List<User> allUsers = userMapper.getAll();
		int allUsersCount = allUsers.size();
		
		List<Integer> attendanceArray = new ArrayList<>();//周1~7出勤人数数组 
		List<Integer> notAttendanceArray = new ArrayList<>();//周1~7缺勤人数数组
		List<String> dateArray = new ArrayList<>();
		for(int i=0; i<=6; i++) {
			Date todayDate = new Date(mondayDate.getTime() + millSecondOfDay*i);
			int count = signMapper.getSignAccountOfDate(todayDate);
			attendanceArray.add(count);//出勤数组添加
			notAttendanceArray.add(allUsersCount-count);//缺勤人数数组添加
			dateArray.add(DateUtil.getStr(todayDate));//日期数组添加
		}
		
		Map<String, Object> result = new HashMap<>();
		result.put("labels", dateArray);
		
		List<Map<String, Object>> datasets = new ArrayList<>();
		Map<String, Object> dataset1 = new HashMap<>();
		dataset1.put("label", "缺勤人数");
		dataset1.put("fillColor", "rgba(210, 214, 222, 1)");
		dataset1.put("strokeColor", "rgba(210, 214, 222, 1)");
		dataset1.put("pointColor", "rgba(210, 214, 222, 1)");
		dataset1.put("pointStrokeColor", "#c1c7d1");
		dataset1.put("pointHighlightFill", "#fff");
		dataset1.put("pointHighlightStroke", "rgba(220,220,220,1)");
		dataset1.put("data", notAttendanceArray);
		datasets.add(dataset1);
		
		Map<String, Object> dataset2 = new HashMap<>();
		dataset2.put("label", "出勤人数");
		dataset2.put("fillColor", "rgba(60,141,188,0.9)");
		dataset2.put("strokeColor", "rgba(60,141,188,0.8)");
		dataset2.put("pointColor", "#3b8bba");
		dataset2.put("pointStrokeColor", "rgba(60,141,188,1)");
		dataset2.put("pointHighlightFill", "#fff");
		dataset2.put("pointHighlightStroke", "rgba(60,141,188,1)");
		dataset2.put("data", attendanceArray);
		datasets.add(dataset2);
		
		result.put("datasets", datasets);
		
		
		return result;
	}

	@Override
	public boolean isSigned(String userId, Date signDate) {
		Calendar todayC = Calendar.getInstance();
		todayC.setTime(signDate);
		Calendar cal = Calendar.getInstance();
		cal.set(todayC.get(Calendar.YEAR), todayC.get(Calendar.MONTH),
						todayC.get(Calendar.DATE), 0, 0, 0);
		System.out.println("*****************11111" + cal.getTime());
		Sign sign = signMapper.getByUserIdAndSignDate(userId, DateUtil.getDate(DateUtil.getStr(cal.getTime())));
		System.out.println(userId+"---sign.............." + sign + "****" + DateUtil.getDate("2019-05-19"));
		if(sign == null) {//如果没查到亲到信息，返回false
			return false;
		}
		else {
			return true;//查到签到信息，返回true
		}
	}

	@Override
	public int userSign(User user) {
		Sign sign = new Sign();
		sign.setUser(user);
		sign.setSignDate(new Date());
		return signMapper.add(sign);
	}

}
