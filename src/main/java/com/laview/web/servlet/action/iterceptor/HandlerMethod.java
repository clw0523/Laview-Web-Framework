package com.laview.web.servlet.action.iterceptor;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.config.UrlMethodInfo;
/**
 * 
 * @Description: TODO
 * @author laview
 * @date 2016年12月31日 下午10:42:21
 *
 */
public class HandlerMethod {

	/**
	 * 请求所映射的类的实例
	 */
	private Object actionInstance;
	
	/**
	 * 请求所映射的方法
	 */
	private UrlMethodInfo handleMethodInfo;
	
	/**
	 * 方法的参数值，（按顺序）
	 */
	private Object[] methodArgs;
	
	/**
	 * 执行 Action 的返回结果
	 */
	private Object actionResult;
	
	
	public HandlerMethod(Object action, UrlMethodInfo methodInfo, Object[] args, Object result) {
		this.actionInstance = action;
		this.handleMethodInfo = methodInfo;
		this.methodArgs = args;
		this.actionResult = result;
	}

	public Object getActionInstance() {
		return actionInstance;
	}

	public void setActionInstance(Object actionInstance) {
		this.actionInstance = actionInstance;
	}

	public UrlMethodInfo getHandleMethodInfo() {
		return handleMethodInfo;
	}

	public void setHandleMethodInfo(UrlMethodInfo handleMethodInfo) {
		this.handleMethodInfo = handleMethodInfo;
	}

	public Object[] getMethodArgs() {
		return methodArgs;
	}

	public void setMethodArgs(Object[] methodArgs) {
		this.methodArgs = methodArgs;
	}

	public Object getActionResult() {
		return actionResult;
	}

	public void setActionResult(Object actionResult) {
		this.actionResult = actionResult;
	}
}
