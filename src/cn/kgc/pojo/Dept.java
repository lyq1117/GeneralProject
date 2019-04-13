package cn.kgc.pojo;

public class Dept {
	
	private int id;
	private String name;//部门名称
	private String leader_id;//部门经理id
	private String location;//工作地点
	private int status;//部门状态(0-可用 1-不可用)

	public Dept() {
	}

	public Dept(int id, String name, String leader_id, String location, int status) {
		super();
		this.id = id;
		this.name = name;
		this.leader_id = leader_id;
		this.location = location;
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

	public String getLeader_id() {
		return leader_id;
	}

	public void setLeader_id(String leader_id) {
		this.leader_id = leader_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
