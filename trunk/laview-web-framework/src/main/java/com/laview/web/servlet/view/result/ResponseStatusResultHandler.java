/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.laview.commons.lang.IntegerUtils;
import com.laview.commons.lang.StringUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;

/**
 * Response 状态处理
 * 
 * 返回一些状态，格式通常是：  status:404  --- 其中 404 就是  Http Server 响应的状态码
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ResponseStatusResultHandler extends AbstractActionResultHandler{
	
	private final static Logger logger = Logger.getLogger(ResponseStatusResultHandler.class);

	private final static String RESULT_PREFIX = "status:";
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#processActionResult(com.laview.web.servlet.ServletData, com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		String view = this.getViewNameFrom(context.getActionResult());
		String[] ss = view.split(":");
		if(ss.length > 0){
			try {
				servletData.getResponse().sendError(IntegerUtils.tryStrToInt(ss[1], 501));
			}
			catch (IOException e) {
				logger.error("[LWF ResponseStatus Handler]==> 状态码类型返回值处理失败，状态码[" + ss[1] + "]，异常信息：", e);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#accept(java.lang.Object)
	 */
	@Override
	public boolean accept(Object actionResult) {
		String view = this.getViewNameFrom(actionResult);
		return (StringUtils.stringNotEmpty(view) && view.startsWith(RESULT_PREFIX));
	}

}