package cn.kgc.pojo;

public class Role {
	
	private String id;//角色编号
	private String name;//角色名称
	private String permission;//角色备注(暂时无用字段)
	
	public Role() {
	}

	public Role(String id, String name, String permission) {
		this.id = id;
		this.name = name;
		this.permission = permission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
