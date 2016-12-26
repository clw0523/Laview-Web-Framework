/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import java.util.Set;

import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.view.ModelAndView;

/**
 * 处理 ModelAndView 的返回类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ModelAndViewResultHandler implements ActionResultHandler{

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#processActionResult(com.laview.web.servlet.ServletData, com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	public ActionForward processActionResult(ServletData servletData, ActionExecuteContext context) {
		ModelAndView actionResult = (ModelAndView)context.getActionResult();
		Set<String> paramNames = actionResult.getModel().keySet();
		
		for(String paramName: paramNames){
			servletData.setRequestAttribute(paramName, actionResult.getModel().get(paramName));
		}
		
		return processDefaultResult(actionResult.getView(), servletData, context);
	}

	/**
	 * 得到  ModelAndView 中的 View 之后，目前版本只交由  DefaultJspResultHandler 进行简单处理
	 *
	 * @param view
	 * @param servletData
	 * @param context
	 * @return
	 */
	private ActionForward processDefaultResult(String view, ServletData servletData, ActionExecuteContext context) {
		return new DefaultJspResultHandler(view).processActionResult(servletData, context);
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.view.result.ActionResultHandler#accept(java.lang.Object)
	 */
	@Override
	public boolean accept(Object actionResult) {
		/**
		 * 如果返回的是  ModelAndView -- 这是 Spring 式的标准返回
		 */
		return actionResult != null && ReflectUtils.isInheritance(actionResult, ModelAndView.class);
	}

}