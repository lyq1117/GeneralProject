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
	
	/**
	 * 获取所有部门集合
	 * @return
	 */
	public List<Dept> getAll();
	
	/**
	 * 保存部门信息
	 * @param dept
	 * @return
	 */
	public int save(Dept dept);
	
	/**
	 * 改变部门状态
	 * @param deptId
	 * @param status
	 * @return
	 */
	public int changeStatus(int deptId, int status);
	
	/**
	 * 新增部门信息
	 * @param dept
	 * @return
	 */
	public int add(Dept dept);
	
	/**
	 * 取消所有部门中部长id为userId的部长id
	 * @param userId
	 * @return
	 */
	public int cancelDeptLeaderId(String userId);
	
	/**
	 * 取消总经办中userId作为部长id
	 * @param userId
	 * @return
	 */
	public int cancelManagerDeptLeaderId(String userId);

}
