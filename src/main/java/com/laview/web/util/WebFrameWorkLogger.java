/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.util;

import org.apache.log4j.Logger;

/**
 * 框架统一日志输出类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebFrameWorkLogger {

	private final static Logger logger = Logger.getLogger("MWF");

	/**
	 *
	 * @param format
	 */
	public static void debug(String message) {
		logger.debug(message);
	}

	/**
	 *
	 * @param string
	 * @param e
	 */
	public static void error(String message, Exception e) {
		logger.error(message, e);
	}
	
	
}