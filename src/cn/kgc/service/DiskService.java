package cn.kgc.service;

import cn.kgc.pojo.Disk;

public interface DiskService {

	/**
	 * 根据全路径获取网盘文件
	 * @param path
	 * @return
	 */
	public Disk getDiskByPath(String path);
	
	/**
	 * 根据id获取Disk
	 * @param id
	 * @return
	 */
	public Disk getDiskById(int id);
	
	/**
	 * 添加一条网盘文件信息
	 * @param disk
	 * @return
	 */
	public int addDisk(Disk disk);
	
	/**
	 * 通过id删除disk网盘文件信息
	 * @param id
	 * @return
	 */
	public int deleteDiskById(int id);
	
	/**
	 * 通过path全路径删除disk网盘文件信息
	 * @param path
	 * @return
	 */
	public int deleteDiskByPath(String path);
	
}
