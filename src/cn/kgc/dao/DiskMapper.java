package cn.kgc.dao;

import cn.kgc.pojo.Disk;

public interface DiskMapper {
	
	/**
	 * 通过全路径获取disk
	 * @param path
	 * @return
	 */
	public Disk getByPath(String path);
	
	/**
	 * 通过id获取disk
	 * @param id
	 * @return
	 */
	public Disk getById(int id);
	
	/**
	 * 添加一条网盘文件信息
	 * @param disk
	 * @return
	 */
	public int add(Disk disk);
	
	/**
	 * 通过id删除disk网盘文件信息
	 * @param id
	 * @return
	 */
	public int deleteById(int id);
	
	/**
	 * 通过path全路径删除disk网盘文件信息
	 * @param path
	 * @return
	 */
	public int deleteByPath(String path);

}
