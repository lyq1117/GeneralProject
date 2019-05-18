package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.Notice;

public interface NoticeService {
	
	/**
	 * 获取所有公告
	 * @return
	 */
	public List<Notice> getAllNotcie();

	/**
	 * 添加公告信息
	 * @param notice
	 * @return
	 */
	public int addNotice(Notice notice);
	
	/**
	 * 通过id获取公告
	 * @param id
	 * @return
	 */
	public Notice getNoticeById(int id);
	
	/**
	 * 根据发布者id获取公告集合
	 * @param submitUserId
	 * @return
	 */
	public List<Notice> getNoticesBySubmitUserId(String submitUserId);
	
}
