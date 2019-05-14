package cn.kgc.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.ProjectNotice;
import cn.kgc.service.ProjectNoticeService;

@Controller
@RequestMapping(value="/page/notice")
public class NoticeController {
	
	@Resource
	private ProjectNoticeService projectNoticeService;
	
	/**
	 * 获取项目的公告集合
	 * @param projectId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getProjectNotices.do",method=RequestMethod.POST)
	public String getProjectNotices(@RequestParam int projectId) {
		//获取项目公告集合
		List<ProjectNotice> list = projectNoticeService.getProjectNotices(projectId);
		
		return JSONArray.toJSONString(list);
	}

}
