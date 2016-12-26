/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.iterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.web.servlet.action.ActionExecuteContext;

/**
 * 一个 Interceptor 抽象类。 可以继承这个类来只实现自己需要的 方法
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class AbstractInterceptor implements Interceptor{

	/* (non-Javadoc)
	 * @see com.laview.web.struts.action.interceptor.Interceptor#prepareProcess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean prepareProcess(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	/**
	 * 已经进入 Framework， 获得了  action，但未开始对 action 进行处理，仍然可以中止系统的执行
	 *
	 * @param request
	 * @param response
	 * @param action
	 */
	@Override
	public boolean beforeProcess(InterceptContext interceptContext){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.action.iterceptor.Interceptor#afterProcess(InterceptContext)
	 */
	@Override
	public void afterProcess(InterceptContext interceptContext) {
		
	}

	/**
	 * 已经执行了 action，但还没有 渲染  View ，也就是说，还没有将 View 所需要的数据给 View
	 *
	 * @param request
	 * @param response
	 * @param context        ------- ActionExecuteContext 封装了  Action, Action 参数 和 Action 执行完成之后的结果
	 */
	@Override
	public void postProcess(HttpServletRequest request, HttpServletResponse response, ActionExecuteContext context){
		
	}
	
	/**
	 * 输出页面执行后进入到这里，能获得Exception了。。。本方法更多用于 异常的捕捉
	 * 
	 *
	 * @param request
	 * @param response
	 * @param action
	 * @param exception
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object action, Exception exception){
		
	}
	
}