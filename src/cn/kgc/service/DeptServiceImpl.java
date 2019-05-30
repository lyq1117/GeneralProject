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

}
