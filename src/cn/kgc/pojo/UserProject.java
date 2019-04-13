package cn.kgc.pojo;

public class UserProject {
	
	private String userId;//用户名
	private int projectId;//项目id
	
	public UserProject() {
	}

	public UserProject(String userId, int projectId) {
		super();
		this.userId = userId;
		this.projectId = projectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
}
