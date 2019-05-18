package cn.kgc.service;

import cn.kgc.pojo.Dept;

public interface DeptService {
	
	/**
	 * 通过id获取部门
	 * @param id
	 * @return
	 */
	public Dept getDeptById(int id);

}
