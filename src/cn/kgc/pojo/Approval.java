package cn.kgc.pojo;

import java.util.Date;

public class Approval {
	
	private String id;//编号 由 年月日+日期的毫秒数 构成
	private int type;//审批类型 0-人事审批 1-财务审批 2-行政审批
	private String title;//审批标题
	private String content;//审批内容
	private String submitUserId; //提交审批的人
	private Date submitDate;//提交审批的日期
	private String approvalUserId;//审批人
	private Date approvalDate;//审批日期
	private int status;//状态 0-带审批 1-已同意 2-已拒绝
	
	public Approval() {
	}

	public Approval(String id, int type, String title, String content, String submitUserId, Date submitDate,
			String approvalUserId, Date approvalDate, int status) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.content = content;
		this.submitUserId = submitUserId;
		this.submitDate = submitDate;
		this.approvalUserId = approvalUserId;
		this.approvalDate = approvalDate;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(String submitUserId) {
		this.submitUserId = submitUserId;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getApprovalUserId() {
		return approvalUserId;
	}

	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
