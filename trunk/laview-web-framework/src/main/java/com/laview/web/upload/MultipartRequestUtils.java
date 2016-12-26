/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.upload;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.laview.web.servlet.commons.GlobalConfig;


/**
 * 文件上传工具类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class MultipartRequestUtils {

	private static final Logger log = Logger.getLogger(MultipartRequestUtils.class);
	
	
	public static String getUploadTempDir(HttpServletRequest request){
		return GlobalConfig.getContextTempDir();
	}
	
	/**
	 * 上传文件的 MultipartRequest 的内容
	 *
	 * @param request
	 * @param argName
	 * @return
	 */
	public static Map<String, Object> getMultipartRequestParameterValues(HttpServletRequest request, String argName) {
		if(log.isDebugEnabled())
			log.info(" * 进入带文件上传 Form 的数据处理方法中。。。");
		
		String encoding = request.getCharacterEncoding();
		
		DiskFileItemFactory uploadFactory = new DiskFileItemFactory();
		File file = new File(getUploadTempDir(request));
		
		if(!file.exists())
			file.mkdirs();
		
		uploadFactory.setRepository(file);
		uploadFactory.setSizeThreshold((int)getUploadMemMaxSize(request));
		
		ServletFileUpload fileUpload = new ServletFileUpload(uploadFactory);
		fileUpload.setFileSizeMax(getUploadMaxSize(request));

		List items = null;  
		try {  
			items = fileUpload.parseRequest(request);  
		} catch (SizeLimitExceededException e) {  
			log.error("上传文件大小超过限制...", e);  
		} catch(Exception e) {  
			log.error("处理上传文件失败...", e);  
		}  
		
		if(items != null){
			encoding = (encoding == null) ? "ISO-8859-1": encoding;
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			try{
				for(Iterator<FileItem> its = items.iterator(); its.hasNext(); ){
					FileItem item = its.next();

					if(item.isFormField()){
						//正常字段
						resultMap.put(item.getFieldName(), item.getString(encoding));
					}else {
						//上传文件字段
						resultMap.put(item.getFieldName(), getFileParameterValue(item));
					}
				}
			}catch(Exception e){
				log.error("获取请求数据失败...", e);
			}
			
			return resultMap;
		}
		
		return null;
	}

	public static long getUploadMaxSize(HttpServletRequest request){
		MultipartRequestHandler handler = new CommonsMultipartRequestHandler();
		
		return handler.getUploadMaxSize();
	}
	
	public static long getUploadMemMaxSize(HttpServletRequest request){
		MultipartRequestHandler handler = new CommonsMultipartRequestHandler();
		
		return handler.getUploadMemMaxSize();
	}

	private static Object getFileParameterValue(FileItem item) {
		return new UploadFormFile(item);
	}
}