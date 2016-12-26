/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.iterceptor;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.laview.web.servlet.action.ActionExecuteContext;

/**
 * 请求拦截器工厂，用来载入并执行的类
 *
 * @author laview_chen
 * @since: v1.0
 */
public class RequestInterceptorFactory {

	private final static Logger logger = Logger.getLogger(RequestInterceptorFactory.class);
		
	private final static Set<Interceptor> interceptors = new CopyOnWriteArraySet<Interceptor>();
	
	/**
	 * 添加拦截器
	 *
	 * @param instance
	 */
	public static void addInterceptor(Interceptor instance){
		interceptors.add(instance);
	}

	/**
	 * 添加拦截器
	 *
	 * @param instance
	 */
	public static void removeInterceptor(Interceptor instance){
		interceptors.remove(instance);
	}
	
	/**
	 * 执行所有拦截器的 prepareProcess 操作
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean triggerPrepareProcess(HttpServletRequest request, HttpServletResponse response){
		
		for(Interceptor interceptor: interceptors){
			try{
				if(!interceptor.prepareProcess(request, response)){
					return false;
				}
			}catch(Exception e){
				logger.error("[LWF]==> 执行拦截器[" + interceptor.getClass().getName() + "#prepareProcess]异常失败，异常信息：", e);
			}
		}
		
		return true;
	}
	
	/**
	 * 在执行 Action 之前触发事件
	 *
	 * @param action
	 * @param args
	 * @return
	 */
	public static boolean triggerBeforeProcess(InterceptContext context){
		for(Interceptor interceptor: interceptors){
			try{
				if(!interceptor.beforeProcess(context)){
					return false;
				}
			}catch(Exception e){
				logger.error("[LWF]==> 执行拦截器[" + interceptor.getClass().getName() + "#beforeProcess]异常失败，Action[" + context.getAction().getClass().getName() + "]，异常信息：", e);
			}
		}
		
		return true;
	}

	/**
	 * 在执行 Action 之后触发事件
	 *
	 * @param action
	 * @param args
	 * @return
	 */
	public static boolean triggerAfterProcess(InterceptContext interceptContext){
		for(Interceptor interceptor: interceptors){
			try{
				interceptor.afterProcess(interceptContext);
			}catch(Exception e){
				logger.error("[LWF]==> 执行拦截器[" + interceptor.getClass().getName() + "#beforeProcess]异常失败，Action[" + interceptContext.getAction().getClass().getName() + "]，异常信息：", e);
			}
		}
		
		return true;
	}
	
	/**
	 * 执行所有拦截器的 postProcess 操作
	 *
	 * @param request
	 * @param response
	 * @param action      请求处理类
	 * @param actionArgs  请求处理方法的传入参数
	 * @param result
	 */
	public static void triggerPostProcess(
						HttpServletRequest request, 
						HttpServletResponse response, ActionExecuteContext context){
		for(Interceptor interceptor: interceptors){
			try{
				interceptor.postProcess(request, response, context);
			}catch(Exception e){
				logger.error("[LWF]==> 执行拦截器[" + interceptor.getClass().getName() + "#postProcess]异常失败，上下文：" + context + "异常信息：", e);
			}
		}
	}

	/**
	 * 触发 异常拦截器
	 *
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	public static void triggerAfterCompletion(
						HttpServletRequest request, 
						HttpServletResponse response, 
						Object action, 
						Exception exception) {
		for(Interceptor interceptor: interceptors){
			try{
				interceptor.afterCompletion(request, response, action, exception);
			}catch(Exception e){
				logger.error("[LWF]==> 执行拦截器[" + interceptor.getClass().getName() + "#afterCompletion]异常失败，Action[" + action.getClass().getName() + "]，异常信息：", e);
			}
		}
		
	}
	
}