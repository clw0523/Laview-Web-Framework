/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;


/**
 * 结果处理接口，实现本接口来处理请求调用返回的结果。
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface ActionResultHandler {

	/**
	 * 处理返回结果，并返回  ActionForward
	 * 
	 * @param actionResult
	 * @param webRequest
	 * @return
	 */
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context);

	/**
	 * 用来说明本 Handler 是否接受和处理 Action Result
	 *
	 *  true -- 表示本 handler 将处理这个 Action Result, 处理完之后，其他 Handler 将不能再进行处理
	 *  
	 *  false -- 表示本 handler 不处理由其他 Handler 进行处理
	 *
	 * @param actionResult
	 * @return
	 */
	public boolean accept(Object actionResult);

}