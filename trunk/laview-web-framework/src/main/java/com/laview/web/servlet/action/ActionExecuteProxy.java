/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.laview.commons.web.RequestMethod;
import com.laview.container.context.annotation.Autowired;
import com.laview.container.core.BeanContainerFactory;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.config.UrlMethodInfo;
import com.laview.web.servlet.action.config.UrlMethodMappings;
import com.laview.web.servlet.action.iterceptor.InterceptContext;
import com.laview.web.servlet.action.iterceptor.RequestInterceptorFactory;
import com.laview.web.servlet.action.support.AfterMethodExecuteHelper;
import com.laview.web.servlet.action.support.BeforeMethodExecuteHelper;
import com.laview.web.servlet.commons.WebResponseConstants;
import com.laview.web.servlet.method.ActionMethodParameterHandler;
import com.laview.web.util.WebFrameWorkLogger;




/**
 * 对 Action 进行具体的 处理：
 * 
 *   1）参数注入与封装
 *   
 *   2）执行 Action
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionExecuteProxy {

	private static final Logger log = Logger.getLogger(ActionExecuteProxy.class);
	
	/**
	 * 执行 Action
	 *
	 * @param servletData
	 * @param action
	 * @return
	 */
	public void execute(ServletData servletData, ActionExecuteContext actionContext) throws Exception{
		
		log.debug("[LWF]==> 进入执行 Pojo Acton, MethodPath 值[" + actionContext.getRequestPath() + "]");
		log.debug("[LWF]==> 进入执行 Pojo Acton, RequestMethod 值[" + actionContext.getRequestMethodPath() + "]");
		
		//获取映射方法配置
		UrlMethodMappings method = getActionUrlMethodMapping(servletData.getRequestMethod(), actionContext);
		
		if(method != null){
			log.debug("[LWF]==> 进入执行 Pojo Acton, 方法名称[" + method.getMethod().getName() + "]");
			
			actionContext.setUrlMethodMapping(method);
			
			//获取参数方法 --- 根据方法参数 与 请求数据，将请求数据组装到成方法所需要的参数
			Object[] args = resolveHandlerArgument(servletData, actionContext);

			//触发 Action 执行前事件
			if(triggerBeforeExecute(servletData, actionContext, args)){
				
				//方法的参数数组
				actionContext.setMethodArgs(args);
				
				//自动注入Autowired的Service到Action实例
				setActionInstanceAutowiredField(actionContext);
				//执行 Action 方法
				Object actionResult = method.getMethod().invoke(actionContext.getActionInstance(), args);
				actionContext.setActionResult(actionResult);
				
				//触发 Action 执行后事件
				triggerAfterExecute(servletData, actionContext, args);
			}else{
				//返回中止结果
				actionContext.setActionResult(WebResponseConstants.SC_FORBIDDEN);
			}
		}
	}

	/**
	 *  自动注入Autowired的Service到Action实例
	 *  
	 * @param actionContext
	 * @throws Exception
	 * @throws IllegalAccessException
	 */
	private void setActionInstanceAutowiredField(ActionExecuteContext actionContext)
			throws Exception, IllegalAccessException {
		Field[] fields = actionContext.getActionInstance().getClass().getDeclaredFields();
		for(Field field:fields){
			if(field.isAnnotationPresent(Autowired.class)){
				field.setAccessible(true);
				field.set(actionContext.getActionInstance(), BeanContainerFactory.getBeanBy(field.getType()) );
			}
		}
	}

	/**
	 * 获取 Action中对应的具体处理方法
	 *
	 * @param requestMethod 
	 * @param actionContext
	 * @return
	 */
	private UrlMethodMappings getActionUrlMethodMapping(RequestMethod requestMethod, ActionExecuteContext actionContext) {
		return actionContext.getUrlMethodMapping() ;//.getMethodConfig(requestMethod);
	}

	/**
	 * 获取由这个方法的 参数按顺序组成的参数数值数组。
	 * 
	 *   *) 先取 方法的参数，然后从 请求提交中找出匹配的数据，这将作为 参数值 注入到 方法中执行
	 * 
	 * @param servletData 
	 * @param method
	 * @return
	 */
	private Object[] resolveHandlerArgument(ServletData servletData, ActionExecuteContext actionContext) throws Exception{
		ActionMethodParameterHandler methodInvoker = new ActionMethodParameterHandler(servletData);
		return methodInvoker.resolveHandlerArgument(actionContext);
	}

	/**
	 *
	 * @param actionInstance
	 * @param args
	 */
	private boolean triggerBeforeExecute(ServletData servletData, ActionExecuteContext actionContext, Object[] args) {
		boolean result = false;
		Object action = null;
		try{
			action = actionContext.getActionInstance();
			UrlMethodInfo methodInfo = (UrlMethodInfo)actionContext.getUrlMethodMapping();
		
			result = RequestInterceptorFactory.triggerBeforeProcess(new InterceptContext(servletData, action, methodInfo, args));
		}catch(Exception e){
			WebFrameWorkLogger.error("[LWF]==> 触发 BeforeExceute 拦截器失败，异常：", e);
		}
		
		if(result && action != null){
			result = beforeMethodExecute(servletData, actionContext, args);
		}
		
		return result;
	}

	/**
	 * 执行请求的 Before 拦截器
	 *
	 * @param actionContext
	 * @param args
	 * @return
	 */
	private boolean beforeMethodExecute(ServletData servletData, ActionExecuteContext actionContext, Object[] args) {
		BeforeMethodExecuteHelper helper = new BeforeMethodExecuteHelper();
		return helper.execute(servletData, actionContext, args);
	}

	/**
	 * 触发 action 执行后拦截操作
	 *
	 * @param actionInstance
	 * @param args
	 * @param actionResult
	 */
	private void triggerAfterExecute(ServletData servletData, ActionExecuteContext actionContext, Object[] args) {
		Object action = null;
		try{
			action = actionContext.getActionInstance();
			UrlMethodInfo methodInfo = (UrlMethodInfo)actionContext.getUrlMethodMapping();

			RequestInterceptorFactory.triggerAfterProcess(new InterceptContext(servletData, action, methodInfo, args, actionContext.getActionResult()));
			
		}catch(Exception e){
			WebFrameWorkLogger.error("[LWF]==> 触发 AfterExceute 拦截器失败，异常：", e);
		}
		
		if(action != null){
			doAfterMethodExecute(servletData, actionContext, args);
		}
	}

	/**
	 *
	 * @param servletData
	 * @param actionContext
	 * @param args
	 */
	private void doAfterMethodExecute(ServletData servletData, ActionExecuteContext actionContext, Object[] args) {
		AfterMethodExecuteHelper helper = new AfterMethodExecuteHelper();
		helper.execute(servletData, actionContext, args);
	}
	
}