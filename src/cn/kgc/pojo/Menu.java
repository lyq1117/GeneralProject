package cn.kgc.pojo;

public class Menu {
	
	private int id;//菜单编号
	private String name;//菜单名称
	private String url;//链接地址
	private String icon;//图片地址
	private int parentId;//父菜单编号
	private String permission;//权限名
	private String mark;//备注   备用字段
	
	public Menu() {
	}

	public Menu(int id, String name, String url, String icon, int parentId, String permission, String mark) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.parentId = parentId;
		this.permission = permission;
		this.mark = mark;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
