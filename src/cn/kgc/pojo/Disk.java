package cn.kgc.pojo;

import java.util.Date;

public class Disk {
	
	private int id;//编号
	private String path;//网盘文件全路径
	private String name;//网盘文件名
	private double size;//文件大小，单位(MB)
	private User user;//上传人
	private Date uploadDate;//上传日期
	
	public Disk() {
	}

	public Disk(int id, String path, String name, double size, User user, Date uploadDate) {
		super();
		this.id = id;
		this.path = path;
		this.name = name;
		this.size = size;
		this.user = user;
		this.uploadDate = uploadDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

}
