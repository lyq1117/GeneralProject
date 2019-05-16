package cn.kgc.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.kgc.pojo.Disk;
import cn.kgc.pojo.User;
import cn.kgc.service.DiskService;
import cn.kgc.util.DateUtil;


@Controller
@RequestMapping(value="/page/file")
public class FileController {
	
	private Logger logger = Logger.getLogger(FileController.class);

	@Resource
	ServletContext context;
	@Resource
	private DiskService diskService;//网盘业务对象
	
	/**
	 * 上传项目(工程)文件(附件)
	 * @param file
	 * @param request
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("my_project_uploadFile") MultipartFile[] file, 
    					   HttpServletRequest request,
    					   @RequestParam int projectId) throws Exception {
 
		System.out.println("文件个数----------------" + file.length);
		Map<String, String> map = new HashMap<>();
        for (int i = 0; i < file.length; i++) {
 
            //判断文件是否为空
            if (!file[i].isEmpty()) {
 
                //获得原文件名
                String fileName = file[i].getOriginalFilename();
                System.out.println("fileName----" + fileName);
                //File.separator表示在 UNIX 系统上，此字段的值为 /；在 Windows 系统上，它为 \，如：C:\tmp\test.txt和tmp/test.txt
                String filePath = context.getRealPath("") + "upload" + File.separator + "project_file" + File.separator + projectId;
                System.out.println("filePath----" + filePath);
                
                File dateDir = new File(filePath);
 
                //判断当前日期文件夹是否存在，不存在创建
                if (!dateDir.exists()) {
                    dateDir.mkdirs();
                }
 
                //文件名由客户端IP地址+系统当前毫秒数组成
                filePath += File.separator + fileName;
 
                // 复制本地文件到服务器
                FileCopyUtils.copy(file[i].getBytes(), new File(filePath));
                
                
 
            } else {
 
                logger.error("文件上传异常");
                map.put("result", "false");
                return JSON.toJSONString(map);
 
            }
 
        }
        map.put("result", "true");
        return JSON.toJSONString(map);
 
    }
	
	/**
	 * 下载项目(工程)文件(附件)
	 * @param request
	 * @param response
	 * @param fileName
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/fileDownload.do")
	public String fileDownload(HttpServletRequest request,
							   HttpServletResponse response,
							   @RequestParam String fileName,
							   @RequestParam int projectId) throws Exception {
		//String name = "abc.txt";
		
		//获取对应文件的路径
        String path = request.getServletContext().getRealPath("") + "upload" + File.separator + "project_file" + File.separator + projectId + File.separator + fileName;

        //将文件读取到输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));

        //设置文件转码
        fileName = URLEncoder.encode(fileName,"UTF-8");

        //解决中文显示乱码
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        //设置响应的类型
        response.setContentType("multipart/form-data");

        //将对应文件读取出来
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
		return null;
	}
	
	/**
	 * 获取项目(工程)的文件(附件)集合
	 * @param projectId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getProjectFiles.do")
	public String getProjectFiles(@RequestParam int projectId,
								                HttpServletRequest request) {
		//获取对应文件的路径
        String path = request.getServletContext().getRealPath("") + "upload" + File.separator + "project_file" + File.separator + projectId;
        File file = new File(path);
        String[] files = file.list();
        if(files == null) {
        	Map<String, String> map = new HashMap<>();
        	map.put("result", "false");
        	return JSON.toJSONString(map);
        }
        List<Map<String, String>> list = new ArrayList<>();
        for(int i=0; i<files.length; i++) {
        	Map<String, String> map = new HashMap<>();
        	map.put("name", files[i]);
        	list.add(map);
        }
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 根据项目id和文件(附件)名删除附件
	 * @param projectId
	 * @param fileName
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteFile.do")
	public String deleteFile(@RequestParam int projectId,
			                 @RequestParam String fileName,
			                 HttpServletRequest request) {
		String path = request.getServletContext().getRealPath("") + "upload" +
			                 File.separator + "project_file" + File.separator + 
			                 projectId + File.separator + fileName;
		File file = new File(path);
		System.out.println("path----------:" + path);
		Map<String, String> map = new HashMap<>();
		if(file.exists() && file.isFile()) {
			file.delete();
			map.put("result", "true");
		}
		else {
			map.put("result", "false");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 获取企业网盘的根目录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getEnterpriseRoot.do",
					method=RequestMethod.POST)
	public String getEnterpriseRoot(HttpServletRequest request) {
		
		String uploadPath = request.getServletContext().getRealPath("") + "upload";
		
		File uploadDictionary = new File(uploadPath);
		
		if(!uploadDictionary.exists())
			uploadDictionary.mkdir();
		
		String path = request.getServletContext().getRealPath("") + "upload" +
                File.separator + "enterprise_file";
		File file = new File(path);
		//若没有企业网盘文件夹，就创建一个
		if(!file.exists()) {
			file.mkdir();
		}
		Map<String, String> map = new HashMap<>();
		map.put("root", path);
		return JSON.toJSONString(map);
	}
	
	@ResponseBody
	@RequestMapping(value="/newDictionary.do",
					method=RequestMethod.POST)
	public String newDictionary(@RequestParam String dicNamePath,
								@RequestParam String dicName) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();//当前操作的用户
		
		File file = new File(dicNamePath);
		
		Map<String, String> map = new HashMap<>();
		if(!file.exists()) {
			file.mkdir();
			map.put("result", "新建成功");
			Disk disk = new Disk();
			disk.setSize(0);
			disk.setName(dicName);
			disk.setPath(dicNamePath);
			disk.setUploadDate(new Date());
			disk.setUser(user);
			diskService.addDisk(disk);
			
			return JSON.toJSONString(map);
		}
		else {
			map.put("result", "新建失败");
			return JSON.toJSONString(map);
		}
	}

	/**
	 * 获取企业网盘某文件夹的所有文件（显示在表格上，所以要封装成bootstrapTable的表格形式）
	 * @param dictionaryPath
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFiles.do",
					method=RequestMethod.GET)
	public String getFiles(@RequestParam String dictionaryPath,
									 HttpServletRequest request) {
		System.out.println("---------" + dictionaryPath);
		List<Map<String, String>> list = new ArrayList<>();
		
		File file = new File(dictionaryPath);
		File[] files = file.listFiles();
		for (File f : files) {
			Map<String, String> map = new HashMap<>();
			//如果是文件夹
			if(f.isDirectory()) {
				Disk disk = diskService.getDiskByPath(f.getAbsolutePath());
				map.put("name", "<img src=\"/GeneralProject/statics/image/file/dictionary.png\" alt=\"Product Image\"><a href=\"javascript:void(0)\" class=\"enterprise_disk_dicBtn\">"+f.getName()+"</a>");
				map.put("size", "");
				map.put("uploadUser", disk.getUser().getUsername()+"-"+disk.getUser().getName());
				map.put("uploadDate", DateUtil.getStr(disk.getUploadDate()));
				map.put("delOption", "<a style=\"color:red;\" href=\"javascritp:void(0)\" class=\"disk_delBtn\" fileId=\""+disk.getId()+"\"><i class=\"fa fa-remove\"></i></a>");
				list.add(map);
				continue;
			}
			//不是文件夹，而是文件
			Disk disk = diskService.getDiskByPath(f.getAbsolutePath());
			String suffix = disk.getName().substring(disk.getName().lastIndexOf(".")+1);
			String[] suffixArr = {"7z","avi","bmp","doc","docx","fla","flv","gif","html",
								  "jpeg","jpg","mkv","mp3","mp4","png","ppt","pptx",
								  "psd","rar","tar","txt","wav","wma","xls","xlsx",
								  "zip"};//常见后缀名数组
			boolean flag = false;
			for (String s : suffixArr) {
				if(s.equals(suffix))
					flag = true;
			}
			if(flag)
				map.put("name", "<img src=\"/GeneralProject/statics/image/file/"+suffix+".png\" alt=\"Product Image\"><a href=\"file/download.do?id="+disk.getId()+"\">"+disk.getName()+"</a>");
			else if(!flag)
				map.put("name", "<img src=\"/GeneralProject/statics/image/file/unknown.png\" alt=\"Product Image\"><a href=\"file/download.do?id="+disk.getId()+"\">"+disk.getName()+"</a>");
			double size = disk.getSize();
			if(size>=(1024*1024)) {
				map.put("size", String.format("%.2f", disk.getSize()/1024/1024) + " MB");
			}
			else if(size>=1024) {
				map.put("size", String.format("%.2f", disk.getSize()/1024) + " KB");
			}
			else if(size<1024) {
				map.put("size", String.format("%.2f", disk.getSize()) + " B");
			}
			map.put("uploadUser", disk.getUser().getUsername() + "-" + disk.getUser().getName());
			map.put("uploadDate", DateUtil.getStr(disk.getUploadDate()));
			map.put("delOption", "<a style=\"color:red;\" href=\"javascritp:void(0)\" class=\"disk_delBtn\" fileId=\""+disk.getId()+"\"><i class=\"fa fa-remove\"></i></a>");
			list.add(map);
		}
		return JSONArray.toJSONString(list);
	}
	
	/**
	 * 下载文件，根据id查询全路径，再下载
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/download.do")
	public String download(@RequestParam int id,
						   HttpServletRequest request,
						   HttpServletResponse response) throws Exception {
		String path = diskService.getDiskById(id).getPath();
		
		File file = new File(path);
		
        //将文件读取到输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(file));

        String fileName = file.getName();
        
        //设置文件转码
        fileName = URLEncoder.encode(fileName,"UTF-8");

        //解决中文显示乱码
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        //设置响应的类型
        response.setContentType("multipart/form-data");

        //将对应文件读取出来
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
		return null;
	}
	
	/**
	 * 上传
	 * @param file
	 * @param hidePath
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/upload.do", method = RequestMethod.POST)
    public String upload(@RequestParam("files") MultipartFile[] file, 
    					 @RequestParam String hidePath,
    					 HttpServletRequest request) throws Exception {
		System.out.println(hidePath+"*******");
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();//当前操作用户，即上传者
		
		Map<String, String> map = new HashMap<>();
		System.out.println("文件个数：" + file.length);
		for (int i = 0; i < file.length; i++) {
            //判断文件是否为空
            if (!file[i].isEmpty()) {
                //获得原文件名
                String fileName = file[i].getOriginalFilename();
                //文件名由客户端IP地址+系统当前毫秒数组成
                hidePath += File.separator + fileName;
                System.out.println("上传文件全地址：" + hidePath);
                // 复制本地文件到服务器
                FileCopyUtils.copy(file[i].getBytes(), new File(hidePath));
                
                //插入一条disk到数据库
                Disk disk = new Disk();
                disk.setName(fileName);//文件名
                disk.setPath(hidePath);//文件全路径
                disk.setSize(file[i].getSize());//文件大小
                disk.setUser(user);//上传人
                disk.setUploadDate(new Date());//上传日期
                //条用网盘业务对象的添加网盘信息方法
                diskService.addDisk(disk);
            } else {
 
                logger.error("文件上传异常");
                map.put("result", "上传失败");
                return JSON.toJSONString(map);
            }
        }
		map.put("result", "上传成功");
		
		return JSON.toJSONString(map);
	}
	
	/**
	 * 删除文件，通过文件id在数据库查询文件的全路径，再删除
	 * @param fileId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete.do",
					method=RequestMethod.POST)
	public String delete(@RequestParam String fileId) {
		//根据id获取disk网盘文件信息
		Disk disk = diskService.getDiskById(Integer.parseInt(fileId));
		File file = new File(disk.getPath());
		Map<String, String> map = new HashMap<>();
		
		if(null!=file && file.exists()) {
			//删除服务器文件，方法里面包括从数据库删除disk信息
			deleteFileUtil(file);
			map.put("result", "删除成功");
		}
		else {
			map.put("result", "删除失败");
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 递归删除文件夹及其下面所有文件的方法
	 * @param dirFile
	 * @return
	 */
	public boolean deleteFileUtil(File dirFile) {
		 // 如果dir对应的文件不存在，则退出
	    if (!dirFile.exists()) {
	        return false;
	    }
	    //如果是文件
	    if (dirFile.isFile()) {
	    	//根据全路径删除数据库disk网盘信息
	    	diskService.deleteDiskByPath(dirFile.getAbsolutePath());
	        return dirFile.delete();
	    } else {//如果是文件夹，遍历里面的文件
	        for (File file : dirFile.listFiles()) {
	        	deleteFileUtil(file);
	        }
	    }
	    //根据全路径删除数据库disk网盘信息
	    diskService.deleteDiskByPath(dirFile.getAbsolutePath());
	    return dirFile.delete();
	}
	
	@ResponseBody
	@RequestMapping(value="/getPersonalRoot.do",method=RequestMethod.POST)
	public String getPersonalRoot(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();//获取当前操作的用户
		
		String uploadPath = request.getServletContext().getRealPath("") + "upload";
		File uploadDictionary = new File(uploadPath);
		if(!uploadDictionary.exists())
			uploadDictionary.mkdir();
		
		String path = request.getServletContext().getRealPath("") + "upload" +
                File.separator + "personal_file";
		File file = new File(path);
		//若没有个人网盘文件夹，就创建一个
		if(!file.exists()) 
			file.mkdir();
		
		String personalPath = request.getServletContext().getRealPath("") + "upload" +
                File.separator + "personal_file" + File.separator + user.getUsername();
		File personnalDic = new File(personalPath);
		//如果没有当前操作用户的文件夹，就创建一个
		if(!personnalDic.exists())
			personnalDic.mkdir();
		
		Map<String, String> map = new HashMap<>();
		map.put("root", personnalDic.getAbsolutePath());
		return JSON.toJSONString(map);
	}
	
}
