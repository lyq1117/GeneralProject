package cn.kgc.service;

import java.util.List;

import cn.kgc.pojo.Dept;

public interface DeptService {
	
	/**
	 * 通过id获取部门
	 * @param id
	 * @return
	 */
	public Dept getDeptById(int id);
	
	/**
	 * 获取所有部门集合
	 * @return
	 */
	public List<Dept> getAllDept();

}
