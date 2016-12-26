/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import com.laview.commons.lang.StringUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;


/**
 * 简单返回一个 JSP 文件
 *
 * @author laview_chen
 * @since: v1.0
 */
public class DefaultJspResultHandler implements ActionResultHandler{
	
	private final String view;
	
	public DefaultJspResultHandler(){view="";}
	
	public DefaultJspResultHandler(String view){
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionResultHandler#processActionResult(java.lang.String, com.laview.common.web.struts.NativeWebRequest)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		String viewFile = (StringUtils.stringIsEmpty(view)?context.actionResultAsString():view);
		return new DefaultJspForwardResult(viewFile);
	}

	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionResultHandler#accept(java.lang.String)
	 */
	@Override
	public boolean accept(Object actionResult) {
		return false;
	}
	
}