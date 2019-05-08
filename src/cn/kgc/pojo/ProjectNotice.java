package cn.kgc.pojo;

import java.util.Date;

public class ProjectNotice {
	
	private int id;//项目公告id
	private String title;//公告标题
	private Date time;//公告时间
	private String content;//公告内容
	private int projectId;//所属项目id
	private User user;//发起人
	
	public ProjectNotice() {
	}

	public ProjectNotice(int id, String title, Date time, String content, int projectId, User user) {
		this.id = id;
		this.title = title;
		this.time = time;
		this.content = content;
		this.projectId = projectId;
		this.user = user;
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
