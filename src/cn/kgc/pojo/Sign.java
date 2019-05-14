package cn.kgc.pojo;

import java.util.Date;

public class Sign {
	
	private int id;
	private User user;//签到用户
	private Date signDate;//签到日期

	public Sign() {
	}

	public Sign(int id, User user, Date signDate) {
		this.id = id;
		this.user = user;
		this.signDate = signDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	
}
