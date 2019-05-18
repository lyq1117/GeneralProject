package cn.kgc.dao;

import java.util.List;

import cn.kgc.pojo.Dept;

public interface DeptMapper {
	
	/**
	 * 通过id获取部门
	 * @param id
	 * @return
	 */
	public Dept getById(int id);
	
	/**
	 * 通过部长id获取部门集合
	 * @param leaderId
	 * @return
	 */
	public List<Dept> getByleaderId(String leaderId);

}
