/**
 * @Copyright:Copyright (c) 1991 - 2015
 * @Company: Laview
 */
package com.laview.web.servlet.action.support;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.commons.collections.Lists;
import com.laview.web.context.WebApplicationContext;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping;
import com.laview.web.servlet.view.ModelAndView;
import com.laview.web.util.WebFrameWorkLogger;

/**
 * AfterMethodExecuteHelper 与  BeforeMethodExecuteHelper 的基类
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class BaseMethodExecuteHelper {

	/**
	 * 执行
	 *
	 * @param actionContext
	 * @param args
	 * @return
	 */
	public boolean execute(ServletData servletData, ActionExecuteContext actionContext, Object[] args) {
		List<UrlInterceptorMethodMapping> mappings = getExecuteMethodMappings(actionContext);
		boolean result = true;
		if(Lists.notEmpty(mappings)){
			for(UrlInterceptorMethodMapping mapping: mappings){
				result &= doExecute(actionContext, servletData, args, mapping);
			}
		}
		
		return result;
	}

	/**
	 * 获取要执行的方法配置类，具体由子类实现
	 *
	 * @return
	 */
	protected abstract List<UrlInterceptorMethodMapping> getExecuteMethodMappings(ActionExecuteContext actionContext);

	/**
	 *
	 * @param actionContext
	 * @param args
	 * @param result
	 * @param mapping
	 * @return
	 */
	private boolean doExecute(ActionExecuteContext actionContext, ServletData servletData, Object[] args, UrlInterceptorMethodMapping mapping) {
		boolean result = true; 
		if(checkExecute(mapping)){
			try{
				Object action = WebApplicationContext.getActionInstance(mapping.getActionConfig());
				Method method = mapping.getMethod();
				if(mapping.hasMethodArguments()){
					
					Class<?>[] clazzes = method.getParameterTypes();
					Object[] argumentValues = new Object[clazzes.length];
					for(int i=0; i < clazzes.length; i++){
						if(clazzes[i].equals(HttpServletRequest.class)){
							argumentValues[i] = servletData.getRequest();
						}else if(clazzes[i].equals(HttpServletResponse.class)){
							argumentValues[i] = servletData.getResponse();
						}else if(clazzes[i].equals(ModelAndView.class)){
							argumentValues[i] = actionContext.getActionResult();
						}else{
							for(Object arg: args){
								if(clazzes[i].equals(arg.getClass())){
									argumentValues[i] = arg;
									break;
								}
							}
						}
					}
					mapping.getMethod().invoke(action, argumentValues);
				}else{
					mapping.getMethod().invoke(action, new Object[0]);
				}
			}catch(Exception e){
				WebFrameWorkLogger.error("[LWF]==> 触发 doAfterMethod 拦截器失败,拦截器：" + mapping + "，异常：", e);
			}
		}
		return result;
	}

	/**
	 *
	 * @param mapping
	 * @return
	 */
	protected abstract boolean checkExecute(UrlInterceptorMethodMapping mapping);
	
}