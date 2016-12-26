/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.upload;

import org.apache.log4j.Logger;

/**
 * 专用于本框架的工具类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class UploadFileUtils {

	private static final Logger log = Logger.getLogger(UploadFileUtils.class);

	//默认最大上传文件大小：250M
    public static final long DEFAULT_SIZE_MAX = 250 * 1024 * 1024;
	
    //默认上传文件时内存缓存大小：256k
    public static final int DEFAULT_SIZE_THRESHOLD = 256 * 1024;

    
    
}