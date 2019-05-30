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
	
	/**
	 * 保存部门信息
	 * @param dept
	 * @return
	 */
	public int saveDeptInfo(Dept dept);
	
	/**
	 * 改变部门状态
	 * @return
	 */
	public int changeDeptStatus(int deptId, int status);
	
	/**
	 * 新增部门
	 * @param dept
	 * @return
	 */
	public int addDept(Dept dept);
	
	/**
	 * 若用户作为某部门部长，取消其作为部门的部长
	 * @param userId
	 * @return
	 */
	public int cancelDeptLeaderId(String userId);
	
	/**
	 * 若用户作为总经办的部长，取消其作为总经办部长
	 * @param userId
	 * @return
	 */
	public int cancelManagerDeptLeaderId(String userId);

}
