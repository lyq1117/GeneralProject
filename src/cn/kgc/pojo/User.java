package cn.kgc.pojo;

public class User {
	
	private String username;//用户名
	private String pwd;//密码
	private String name;//真实姓名
	private String icon;//头像地址
	private String tel;//联系电话
	private int status;//状态 0-可用 1-禁用
	private Dept dept;//所属部门
	
	public User() {
	}

	public User(String username, String pwd, String name, String icon, String tel, int status, Dept dept) {
		super();
		this.username = username;
		this.pwd = pwd;
		this.name = name;
		this.icon = icon;
		this.tel = tel;
		this.status = status;
		this.dept = dept;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}
}
