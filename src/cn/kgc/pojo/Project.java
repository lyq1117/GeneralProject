package cn.kgc.pojo;

import java.util.Date;

public class Project {
	
	private int id;
	private String name;
	private String description;//工程描述
	private Date createTime;//工程创建时间
	private int duration;//工程工期(单位：天)
	private User user;//工程经理
	private int status;//工程状态(0-创建 1-执行中 2-搁置、非正常结束 3-正常结束)
	
	public Project() {
	}

	public Project(int id, String name, String description, Date createTime, int duration, User user, int status) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.createTime = createTime;
		this.duration = duration;
		this.user = user;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
