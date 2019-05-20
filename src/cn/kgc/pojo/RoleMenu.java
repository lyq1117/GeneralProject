package cn.kgc.pojo;

public class RoleMenu {
	
	private String roleId;//角色id
	private int menuId;//菜单id
	
	public RoleMenu() {
	}

	public RoleMenu(String roleId, int menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

}
