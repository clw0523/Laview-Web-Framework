/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action;

import com.laview.web.servlet.ServletData;

/**
 * Action 执行后返回的结果
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface ActionResultProcessor {

	/**
	 * 处理结果
	 *
	 * @param servletData
	 * @param actionContext
	 * @return 返回  False 表示无法进行处理，返回 True 表示成功处理
	 */
	boolean process(ServletData servletData, ActionExecuteContext actionContext) throws Exception;
}