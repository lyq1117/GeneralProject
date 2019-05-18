package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.NoticeMapper;
import cn.kgc.pojo.Notice;

@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Resource
	private NoticeMapper noticeMapper;

	@Override
	public List<Notice> getAllNotcie() {
		return noticeMapper.getAll();
	}

	@Override
	public int addNotice(Notice notice) {
		return noticeMapper.add(notice);
	}

	@Override
	public Notice getNoticeById(int id) {
		return noticeMapper.getById(id);
	}

	@Override
	public List<Notice> getNoticesBySubmitUserId(String submitUserId) {
		return noticeMapper.getBySubmitUserId(submitUserId);
	}

}
