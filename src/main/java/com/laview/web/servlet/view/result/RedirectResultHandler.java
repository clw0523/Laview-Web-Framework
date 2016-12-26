/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import org.apache.log4j.Logger;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.view.ModelAndView;


/**
 * 处理 redirect 操作，通常的使用方式就是：
 * 
 * 	redirect：url
 *
 * @author laview_chen
 * @since: v1.0
 */
public class RedirectResultHandler extends AbstractActionResultHandler{

	private final static Logger logger = Logger.getLogger(RedirectResultHandler.class);
	
	private final static String REDIRECT_PREFIX = "redirect:";
		
	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionResultHandler#processActionResult(java.lang.String, com.laview.common.web.struts.NativeWebRequest)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		String actionResult = null;
		if(context.getActionResult() instanceof ModelAndView){
			ModelAndView mv = (ModelAndView)context.getActionResult();
			this.renderView(servletData, mv);
			actionResult = mv.getView();
		}else{
			actionResult = context.actionResultAsString();
		}
		if(logger.isDebugEnabled())
			logger.info("[Laview-Web-Struts]==> Redirect 操作 ..., actionResult=" + actionResult + ", JSP:" + actionResult.substring(REDIRECT_PREFIX.length()));
		return new DefaultJspForwardResult(actionResult.substring(REDIRECT_PREFIX.length()), true);
	}

	/* (non-Javadoc)
	 * @see com.laview.common.web.struts.result.ActionResultHandler#accept(java.lang.String)
	 */
	@Override
	public boolean accept(Object actionResult) {
		if(actionResult != null){
			String r = getViewNameFrom(actionResult);
			
			return r.toLowerCase().startsWith(REDIRECT_PREFIX);
		}
		
		return false;
	}
	
}