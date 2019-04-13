package cn.kgc.pojo;

import java.util.Date;

public class Block {
	
	private int id;//工程小块编号
	private User leader;//工程小块负责人
	private String description;//工程小块描述
	private Date createTime;//开始时间
	private int duration;//工期(单位：天)
	private int status;//状态(0-创建 1-执行中 2-非正常结束 3-正常结束)
	private int projectId;//所属工程编号
	
	public Block() {
	}

	public Block(int id, User leader, String description, Date createTime, int duration, int status, int projectId) {
		super();
		this.id = id;
		this.leader = leader;
		this.description = description;
		this.createTime = createTime;
		this.duration = duration;
		this.status = status;
		this.projectId = projectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

}
