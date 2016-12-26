/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import org.apache.log4j.Logger;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.view.ModelAndView;


/**
 * 处理  “Action:index.do” 这样的跳转，用来明确转到一个 Action  这个类的思路有问题
 * 
 * 转到 Action 是不能使用  Redirect 的，这样无法将  request 和 response 传给 它
 *
 * @author laview_chen
 * @since: v1.0
 */
public class RedirectToActionHandler extends AbstractActionResultHandler{

	private final static Logger logger = Logger.getLogger(RedirectToActionHandler.class);
	
	private final static String ACTION_PREFIX = "action:";
	
	/* (non-Javadoc)
	 * @see com.laview.web.struts.action.view.result.ActionResultHandler#processActionResult(java.lang.String, com.laview.web.struts.action.WebRequestAdapter)
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
			logger.info("[Laview-Web-Struts]==> Action Redirect 操作 ..., actionResult=" + actionResult);
		//return new DefaultJspForwardResult(actionResult.substring(REDIRECT_PREFIX.length()), true);
		final String action = actionResult.substring(ACTION_PREFIX.length());
		return new ActionForward(){

			@Override
			public String getView() {
				String result = action;
				if(action.length() > 0 && action.charAt(0) != '/')
					result = "/" + action; 
					
				return result;
			}

			@Override
			public boolean isRedirect() {
				return false;
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see com.laview.web.struts.action.view.result.ActionResultHandler#accept(java.lang.String)
	 */
	@Override
	public boolean accept(Object actionResult) {
		if(actionResult != null){
			String r = getViewNameFrom(actionResult);
		
			return r.toLowerCase().startsWith(ACTION_PREFIX);
		}
		
		return false;
	}

}