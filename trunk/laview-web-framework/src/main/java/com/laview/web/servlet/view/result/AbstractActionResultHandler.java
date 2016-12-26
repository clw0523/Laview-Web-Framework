/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.view.result;

import java.util.Set;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.view.ModelAndView;

/**
 * 提供一个从结果中抽取，由字符串表达的名称 方法
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class AbstractActionResultHandler implements ActionResultHandler{

	/**
	 * 从 结果中提取 Action View 的名称
	 *
	 * @param actionResult
	 * @return
	 */
	protected String getViewNameFrom(Object actionResult){
		if (actionResult instanceof String){
			return (String)actionResult;
		}
		
		if(actionResult instanceof ModelAndView){
			return ((ModelAndView)actionResult).getView();
		}
		
		return "";
	}
	
	/**
	 * 将 ModelAndView 渲染到 Request
	 *
	 * @param servletData
	 * @param actionResult
	 */
	protected void renderView(ServletData servletData, ModelAndView actionResult){
		Set<String> paramNames = actionResult.getModel().keySet();
		
		for(String paramName: paramNames){
			servletData.setRequestAttribute(paramName, actionResult.getModel().get(paramName));
		}
		
	}
}