package cn.kgc.pojo;

import java.util.Date;

public class Notice {
	
	private int id;//编号
	private String title;//标题
	private String content;//内容
	private Date submitDate;//发布日期
	private String submitUserId;//发布人的id
	private int status;//状态 1-紧急 2-一般
	
	public Notice() {
	}

	public Notice(int id, String title, String content, Date submitDate, String submitUserId, int status) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.submitDate = submitDate;
		this.submitUserId = submitUserId;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getSubmitUserId() {
		return submitUserId;
	}

	public void setSubmitUserId(String submitUserId) {
		this.submitUserId = submitUserId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
