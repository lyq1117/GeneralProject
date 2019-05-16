package cn.kgc.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.kgc.dao.DiskMapper;
import cn.kgc.pojo.Disk;

@Service
public class DiskServiceImpl implements DiskService {
	
	@Resource
	private DiskMapper diskMapper;

	@Override
	public Disk getDiskByPath(String path) {
		return diskMapper.getByPath(path);
	}

	@Override
	public Disk getDiskById(int id) {
		return diskMapper.getById(id);
	}

	@Override
	public int addDisk(Disk disk) {
		return diskMapper.add(disk);
	}

	@Override
	public int deleteDiskById(int id) {
		return diskMapper.deleteById(id);
	}

	@Override
	public int deleteDiskByPath(String path) {
		return diskMapper.deleteByPath(path);
	}

}
