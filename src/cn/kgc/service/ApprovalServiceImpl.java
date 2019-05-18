package cn.kgc.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import cn.kgc.dao.ApprovalMapper;
import cn.kgc.pojo.Approval;

@Service
public class ApprovalServiceImpl implements ApprovalService {
	
	@Resource
	private ApprovalMapper approvalMapper;

	@Override
	public List<Map<String, Object>> getApprovalModal(String modalPath) {
		SAXReader reader = new SAXReader();
		File file = new File(modalPath);
		List<Map<String, Object>> result = new ArrayList<>();
		try {
			Document document = reader.read(file);
			//获取根结点
			Element root = document.getRootElement();
			//遍历迭代根结点下的节点
			Iterator it = root.elementIterator();
			while(it.hasNext()) {
				Map<String, Object> map = new HashMap<>();
				Element approval = (Element) it.next();
				String name = approval.attributeValue("name");
				map.put("title", name);
				
				List<Element> items = approval.elements();
				List<String> itemsList = new ArrayList<>();//用于返回的items集合
				for (Element element : items) {
					itemsList.add(element.getText());
				}
				map.put("items", itemsList);
				result.add(map);
			}
			
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public int addApproval(Approval approval) {
		return approvalMapper.add(approval);
	}

	@Override
	public List<Approval> getApprovalsByApprovalUserId(String approvalUserId) {
		return approvalMapper.getByApprovalUserId(approvalUserId);
	}

	@Override
	public Approval getApprovalById(String approvalId) {
		return approvalMapper.getById(approvalId);
	}

	@Override
	public int confirmApproval(String approvalId, Date approvalDate) {
		return approvalMapper.updateStatus(approvalId, approvalDate, 1);
	}

	@Override
	public int rejectApproval(String approvalId, Date approvalDate) {
		return approvalMapper.updateStatus(approvalId, approvalDate, 2);
	}

	@Override
	public List<Approval> getApprovalsBySubmitUserId(String submitUserId) {
		return approvalMapper.getBySubmitUserId(submitUserId);
	}

}
