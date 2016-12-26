/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.iterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laview.web.servlet.action.ActionExecuteContext;

/**
 * 请求拦截器，我们可以实现这个拦截器来实现一些全局性的工作，例如 简单的登录检查等
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface Interceptor {

	/**
	 * 在处理请求之前执行一些动作，返回 false 表示主流程不再执行下去，true 表示主流程继续。
	 * 
	 * 当前的执行路线，并没有进入 Framework 内部
	 * 
	 * 例如，判断是否合法登录等
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean prepareProcess(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 已经进入 Framework， 获得了  action，并处理好 Action 的注入数据，但未执行 action，仍然可以中止系统的执行
	 *
	 * @param InterceptContext  -- 拦截点的上下文信息
	 */
	public boolean beforeProcess(InterceptContext interceptContext);
	
	/**
	 * 刚执行完 Action
	 *
	 * @param InterceptContext -- 拦截点的上下文信息
	 */
	public void afterProcess(InterceptContext interceptContext);
	
	/**
	 * 已经执行了 action，但还没有 渲染  View ，也就是说，还没有将 View 所需要的数据给 View
	 *
	 * @param request
	 * @param response
	 * @param context        ------- ActionExecuteContext 封装了  Action, Action 参数 和 Action 执行完成之后的结果
	 */
	public void postProcess(HttpServletRequest request, HttpServletResponse response, ActionExecuteContext context);
	
	/**
	 * 输出页面执行后进入到这里，能获得Exception了。。。本方法更多用于 异常的捕捉
	 * 
	 *
	 * @param request
	 * @param response
	 * @param action
	 * @param exception
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object action, Exception exception);
	
}