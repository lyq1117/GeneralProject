package cn.kgc.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.DeptMapper;
import cn.kgc.pojo.Dept;

@Service
public class DeptServiceImpl implements DeptService {
	
	@Resource
	private DeptMapper deptMapper;

	@Override
	public Dept getDeptById(int id) {
		return deptMapper.getById(id);
	}

	@Override
	public List<Dept> getAllDept() {
		return deptMapper.getAll();
	}

	@Override
	public int saveDeptInfo(Dept dept) {
		return deptMapper.save(dept);
	}

	@Override
	public int changeDeptStatus(int deptId, int status) {
		return deptMapper.changeStatus(deptId, status);
	}

	@Override
	public int addDept(Dept dept) {
		return deptMapper.add(dept);
	}

	@Override
	public int cancelDeptLeaderId(String userId) {
		return deptMapper.cancelDeptLeaderId(userId);
	}

	@Override
	public int cancelManagerDeptLeaderId(String userId) {
		return deptMapper.cancelManagerDeptLeaderId(userId);
	}


}
