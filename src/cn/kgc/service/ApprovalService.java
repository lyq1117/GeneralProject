package cn.kgc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.kgc.pojo.Approval;

public interface ApprovalService {
	
	/**
	 * 获取审批模板
	 * @param modalPath 模板全路径
	 * @return
	 */
	public List<Map<String, Object>> getApprovalModal(String modalPath);
	
	/**
	 * 添加审批信息
	 * @param approval
	 */
	public int addApproval(Approval approval);
	
	/**
	 * 通过审批人id获取审批集合
	 * @param approvalUserId
	 * @return
	 */
	public List<Approval> getApprovalsByApprovalUserId(String approvalUserId);
	
	/**
	 * 根据id查询审批信息
	 * @param approvalId
	 * @return
	 */
	public Approval getApprovalById(String approvalId);
	
	/**
	 * 同意审批
	 * @param approvalId
	 * @param approvalDate
	 * @return
	 */
	public int confirmApproval(String approvalId, Date approvalDate);
	
	/**
	 * 拒绝审批
	 * @param approvalId
	 * @param approvalDate
	 * @return
	 */
	public int rejectApproval(String approvalId, Date approvalDate);
	
	/**
	 * 通过提交人的用户名获取审批集合
	 * @param submitUserId
	 * @return
	 */
	public List<Approval> getApprovalsBySubmitUserId(String submitUserId);

}
