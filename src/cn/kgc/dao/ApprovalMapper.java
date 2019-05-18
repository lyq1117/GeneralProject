package cn.kgc.dao;

import java.util.Date;
import java.util.List;

import cn.kgc.pojo.Approval;

public interface ApprovalMapper {
	
	/**
	 * 添加审批
	 * @param approval
	 * @return
	 */
	public int add(Approval approval);
	
	/**
	 * 通过审批人id获取审批集合
	 * @param userId
	 * @return
	 */
	public List<Approval> getByApprovalUserId(String approvalUserId);
	
	/**
	 * 根据id获取审批信息
	 * @param approvalId
	 * @return
	 */
	public Approval getById(String approvalId);
	
	/**
	 * 根据编号改变审批状态
	 * @param approvalId
	 * @param approvalDate
	 * @return
	 */
	public int updateStatus(String approvalId, Date approvalDate, int status);
	
	/**
	 * 通过提交人id获取审批集合
	 * @param submitUserId
	 * @return
	 */
	public List<Approval> getBySubmitUserId(String submitUserId);

}
