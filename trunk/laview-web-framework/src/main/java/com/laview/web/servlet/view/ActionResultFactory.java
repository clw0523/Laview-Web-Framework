/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.view;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.view.result.ActionForward;
import com.laview.web.servlet.view.result.ActionResultHandler;
import com.laview.web.servlet.view.result.DefaultJspResultHandler;
import com.laview.web.servlet.view.result.FileDownloadResultHandler;
import com.laview.web.servlet.view.result.ModelAndViewResultHandler;
import com.laview.web.servlet.view.result.RedirectResultHandler;
import com.laview.web.servlet.view.result.RedirectToActionHandler;
import com.laview.web.servlet.view.result.ResponseStatusResultHandler;



/**
 * Action 返回结果 的处理工厂
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionResultFactory {

	private static final Logger log = Logger.getLogger(ActionResultFactory.class);
	
	private final static List<ActionResultHandler> actionResultHandlers = 
								new CopyOnWriteArrayList<ActionResultHandler>();
	
	static{
		actionResultHandlers.add(new RedirectResultHandler());
		actionResultHandlers.add(new RedirectToActionHandler());
		actionResultHandlers.add(new ResponseStatusResultHandler());
		actionResultHandlers.add(new FileDownloadResultHandler());
		actionResultHandlers.add(new ModelAndViewResultHandler());
	}
	
	/**
	 * 对返回结果 进行分类处理
	 *
	 * @param result
	 * @param servletData
	 */
	public static boolean processActionResult(ServletData servletData, ActionExecuteContext context) {
		Object actionResult = context.getActionResult();
		if(log.isDebugEnabled())
			log.info("Process Action Result:" + actionResult);
		
		return processActionResultByHandler(servletData, context);
		
	}

	/**
	 * 使用注册 的 Handler 来处理，采用“短路处理”方式，只要有一个 Handler 能够处理，就结束遍历
	 *
	 * @param servletData
	 * @param context
	 * @return
	 */
	private static boolean processActionResultByHandler(ServletData servletData, ActionExecuteContext context) {

		ActionForward forwardResult = null;
		boolean hasDo = false;
		for(ActionResultHandler handler:actionResultHandlers){
			if(handler.accept(context.getActionResult())){
				forwardResult = handler.processActionResult(servletData, context);
				hasDo = true;
				break;
			}
		}
		
		if(hasDo){
			context.setActionForward(forwardResult);
			return true;
		}else {
			
			//如果无法处理，而返回的结果是一个字符串时，将作
			if(context.actionResultIsString()){
				return processDefaultResult(context.actionResultAsString(), servletData, context);
			}
				
		}
		
		return false;
	}

	/**
	 * 在对结果无法处理情况下，默认将作为 jsp 文件返回。（如果不是这样，当结果真的是 jsp 文件时，就无法返回正确的 jsp文件）
	 *
	 * @param actionResult
	 * @param webRequest
	 * @return
	 */
	private static boolean processDefaultResult(String actionResult, ServletData servletData, ActionExecuteContext context) {
		ActionForward forwardResult = new DefaultJspResultHandler(actionResult).processActionResult(servletData, context);
		
		if(log.isDebugEnabled())
			log.info(" * 返回 JSP File:" + forwardResult.getView());
		
		context.setActionForward(forwardResult);
		
		return true;
	}
	
}