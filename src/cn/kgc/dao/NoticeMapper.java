package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Notice;

public interface NoticeMapper {
	
	/**
	 * 获取所有公告
	 * @return
	 */
	public List<Notice> getAll();
	
	/**
	 * 添加公告
	 * @return
	 */
	public int add(Notice notice);
	
	/**
	 * 根据id获取公告
	 * @param id
	 * @return
	 */
	public Notice getById(int id);
	
	/**
	 * 通过发布者查询公告集合
	 * @param submitUserId
	 * @return
	 */
	public List<Notice> getBySubmitUserId(String submitUserId);

}
