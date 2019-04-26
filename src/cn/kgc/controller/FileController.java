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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


@Controller
@RequestMapping(value="/page/file")
public class FileController {
	
	private Logger logger = Logger.getLogger(FileController.class);

	@Resource
	ServletContext context;
	
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

	
}
