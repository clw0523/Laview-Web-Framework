/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.laview.commons.file.FilenameUtils;
import com.laview.commons.io.IOUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;

/**
 * 返回 文件下载对象 (FileDownloadResult) 的处理类
 * 
 * 文件名的处理： 将 UTF-8 的编码，转为  ISO8859-1 ，要注意。
 *
 * @author laview_chen
 * @since: v1.0
 */
public class FileDownloadResultHandler implements ActionResultHandler{

	private final static Logger logger = Logger.getLogger(FileDownloadResultHandler.class);
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#processActionResult(com.laview.web.servlet.ServletData, com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		FileDownloadResult fileDownload = (FileDownloadResult)context.getActionResult();
		String fileFullName = fileDownload.getFileFullName();
		String filename = FilenameUtils.getFilename(fileFullName);
		
		logger.debug("[LWF FileDownload]==> Download File[" + filename + "]");
		
		try{
			HttpServletResponse response = servletData.getResponse();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);//new String(filename.getBytes("UTF-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileDownload.getFileSize()));

			copyFileToResponse(fileFullName, response.getOutputStream());
		}catch(Exception e){
			ReflectUtils.convertReflectionExceptionToUnchecked(e);
		}
		return null;
	}

	/**
	 *
	 * @param fileFullName
	 * @param outputStream
	 */
	private void copyFileToResponse(String fileFullName, OutputStream outputStream) throws Exception{
		File file = new File(fileFullName);
		if(file.exists()){
			InputStream fileData = new BufferedInputStream(new FileInputStream(file));
			try{
				IOUtils.copy(fileData, outputStream);
			}finally{
				if(fileData != null){
					try{
						fileData.close();
					}catch(Exception e){
						logger.error("Close [" + fileFullName + "] InputStream Fail!", e);
					}
				}
				
				if(outputStream != null){
					try{
						outputStream.flush();
						outputStream.close();
					}catch(Exception e){
						logger.error("Close [" + fileFullName + "] Response OutputStream Fail!", e);
					}
				}
			}
		}else{
			throw new FileNotFoundException("File [" + fileFullName + "] Not Found!");
		}
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#accept(java.lang.Object)
	 */
	@Override
	public boolean accept(Object actionResult) {
		return actionResult != null && ReflectUtils.isImplements(actionResult.getClass(), FileDownloadResult.class);
	}

}