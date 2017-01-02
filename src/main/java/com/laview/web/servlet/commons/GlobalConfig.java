/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.commons;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.laview.commons.file.FilenameUtils;
import com.laview.web.servlet.DispatchManager;

/**
 * 全局配置
 *
 * @author laview_chen
 * @since: v1.0
 */
public final class GlobalConfig {

	/**
	 * 最大的上传尺寸
	 */
	private static String maxFileSize = "250M";

	/**
	 * 读进内存的文件内容大小
	 */
	private static String memFileSize = "256K";

	/**
	 * view 根目录，默认为 "/WEB-INF/views/" ，注意最后带“/“
	 */
	private static String viewRoot="/WEB-INF/views/";

	/**
	 * 根目录 
	 */
	private static String realRootPath;

	/**
	 * 临时目录
	 */
	private static String contextTempDir;

	private static ServletContext context;
	
	/**
	 * 应用名称
	 */
	private static String contextPath;
	
	/**
	 * 是否开启 直接映射到 jsp 的开关，true 表示提供这项功能， false 表示不提供这项功能
	 */
	private static boolean openDirectToJsp = false;
	
	private static boolean openDirectToHtml = false;
	
	/**
	 *
	 * @param config
	 */
	public static void initGlobalConfig(ServletConfig config) {
		context = config.getServletContext();
		realRootPath = context.getRealPath("/");
		contextPath = context.getContextPath();
	}

	/**
	 * JSP View 的绝对路径
	 *
	 * @return
	 */
	public static String getViewAbstractPath(){
		return FilenameUtils.delLastSeparatorIfExists(realRootPath) + viewRoot;
	}
	
	public final static String getContextTempDir(){
		takeTempDir(context);
		return contextTempDir;
	}

	public final static void setContextTempDir(String tempDir){
		contextTempDir = tempDir;
	}	

	/**
	 * 获取系统定义的临时目录
	 *
	 * @param context
	 */
	private final static void takeTempDir(ServletContext context){
		if(contextTempDir == null){
			File tempDirFile =
					(File) context.getAttribute("javax.servlet.context.tempdir");

			String tempDir = tempDirFile.getAbsolutePath();

			if ((tempDir == null) || (tempDir.length() == 0)) {
				tempDir = System.getProperty("java.io.tmpdir");
			}

			contextTempDir = tempDir;
		}
	}

	/**
	 * @return the viewRoot
	 */
	public static String getViewRoot() {
		return viewRoot;
	}

	/**
	 * @param viewRoot the viewRoot to set
	 */
	public static void setViewRoot(String viewRoot) {
		GlobalConfig.viewRoot = viewRoot;
	}

	/**
	 * @return the realRootPath
	 */
	public static String getRealRootPath() {
		return realRootPath;
	}

	/**
	 * @param realRootPath the realRootPath to set
	 */
	public static void setRealRootPath(String realRootPath) {
		GlobalConfig.realRootPath = realRootPath;
	}

	/**
	 * @return the maxFileSize
	 */
	public static String getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * @param maxFileSize the maxFileSize to set
	 */
	public static void setMaxFileSize(String maxFileSize) {
		GlobalConfig.maxFileSize = maxFileSize;
	}

	/**
	 * @return the memFileSize
	 */
	public static String getMemFileSize() {
		return memFileSize;
	}

	/**
	 * @param memFileSize the memFileSize to set
	 */
	public static void setMemFileSize(String memFileSize) {
		GlobalConfig.memFileSize = memFileSize;
	}
	
	/**
	 * @return the openDirectToJsp
	 */
	public static boolean isOpenDirectToJsp() {
		return openDirectToJsp;
	}
	
	/**
	 * @return the openDirectToHtml
	 */
	public static boolean isOpenDirectToHtml() {
		return openDirectToHtml;
	}

	/**
	 * @param openDirectToJsp the openDirectToJsp to set
	 */
	public static void setOpenDirectToJsp(boolean openDirectToJsp) {
		GlobalConfig.openDirectToJsp = openDirectToJsp;
	}
	
	/**
	 * @param openDirectToJsp the openDirectToJsp to set
	 */
	public static void setOpenDirectToHtml(boolean openDirectToHtml) {
		GlobalConfig.openDirectToHtml = openDirectToHtml;
	}

	private GlobalConfig(){}
	
	private static DispatchManager dispatchManager;
	
	public static void addStaticResourceMapping(String key,String value) {
		dispatchManager.getUrlHandlerMapping().addStaticResourceMapping(key, value);
	}

	public static void setDispatchManager(DispatchManager dispatchManager) {
		GlobalConfig.dispatchManager = dispatchManager;
	}

	public static String getContextPath() {
		return contextPath;
	}

}