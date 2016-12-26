/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.support;

import java.util.Map;

import org.apache.log4j.Logger;

import com.laview.commons.lang.ReflectUtils;
import com.laview.commons.web.RequestContentType;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.ActionResultProcessor;
import com.laview.web.servlet.view.ActionResultFactory;
import com.laview.web.servlet.view.ModelAndView;


/**
 * ActionForward 的一个默认实现
 *
 * @author laview_chen
 * @since: v1.0
 */
public class DefaultActionResultProcessor implements ActionResultProcessor{

	private static final Logger logger = Logger.getLogger(DefaultActionResultProcessor.class);
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.ActionResultProcessor#process(com.laview.web.servlet.ServletData, com.laview.web.servlet.action.ActionExecuteContext)
	 */
	@Override
	public boolean process(ServletData servletData, ActionExecuteContext actionContext) throws Exception{
		
		if(actionContext.hasActionResult()){

			logger.info("[LWF]==> Action 方法带有返回值，返回值[" + actionContext.getActionResult() + "]");
			
			/**
			 * 如果方法定义了 {@ResponseBody}，则返回结果将使用请求类型 来进行处理
			 */
			if(actionContext.defineResponseBody()){
				RequestContentType contentType = servletData.getContentType();
				
				switch(contentType){
					case REQUEST_HTML:
					case REQUEST_TEXT:{
						writeContentToResponse(servletData, actionContext.actionResultAsString());
						return true;
					}
					//case REQUEST_JSON:{
					default:{
						writeContentToResponse(servletData, actionContext.actionResultAsJson());
						return true;
					}
				}
			}
			
			//处理返回：Map --- 在这里会将 Map 自动转换成一个 Json 
			if(ReflectUtils.isInheritance(actionContext.getActionResult(), Map.class)){				
				writeContentToResponse(servletData, actionContext.actionResultAsJson());
				return true;
			}
			
			return ActionResultFactory.processActionResult(servletData, actionContext);

		}
		
		//TODO 返回一个 false 值并不是最终的结果 (在 null 的情况下，Struts1 并不会跳转，实现 ajax 的返回功能）
		return false;
		
	}

	/**
	 * 直接将返回内容写到  Response ( 通常是 AJAX 请求的工作方式 )
	 *
	 * @param servletData
	 * @param actionResultAsString
	 */
	private void writeContentToResponse(ServletData servletData, String actionResultAsString) throws Exception{
		servletData.writeResultToResponse(actionResultAsString);
	}

}