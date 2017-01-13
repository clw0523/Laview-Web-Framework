/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laview.commons.collections.Lists;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.RequestMethod;
import com.laview.web.context.WebApplicationContext;
import com.laview.web.servlet.action.config.ActionConfig;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping;
import com.laview.web.servlet.action.config.UrlMethodMappings;
import com.laview.web.servlet.commons.GlobalConfig;
import com.laview.web.servlet.util.MiscUtils;
import com.laview.web.servlet.view.result.ActionForward;

/**
 * Action 执行的上下文
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionExecuteContext {

	private ActionConfig actionConfig;
	
	/**
	 * 方法请求 （即方法注解上的 请求路径）
	 */
	private final String requestMethodPath;

	/**
	 * 映射方法配置
	 */
	private UrlMethodMappings methodMapping;
	
	/**
	 * 请求拦截方法
	 */
	private Map<ActionConfig, List<UrlInterceptorMethodMapping>> interceptorMappings;

	/**
	 * 方法参数值数组，按方法的参数顺序排列
	 */
	private Object[] methodArgs;

	/**
	 * Action 执行之后的返回结果
	 */
	private Object actionResult;

	/**
	 * 最终的处理结果
	 */
	private ActionForward actionForward;
	
	/**
	 * 执行action触发的异常，没有异常代表执行正常
	 */
	private Exception actionException;
	
	private boolean exceptonHandlerHasResponseBody = false;
	
	/**
	 * action要执行的方法
	 */
	private Method method;
	
	/**
	 * @param config
	 * @param sb
	 */
	public ActionExecuteContext(List<ActionConfig> configs, RequestMethod requestMethod, String methodPath) {
		this.requestMethodPath = methodPath;
		getActionConfigAndRequestMappingBy(configs, requestMethod);
	}
	
	public ActionExecuteContext(){
		this(null, null, null);
	}

	/**
	 * 使用方法的请求路径来确定 ActionConfig 和 UrlRequestMappings
	 *
	 * @param configs
	 * @param methodPath
	 */
	private void getActionConfigAndRequestMappingBy(List<ActionConfig> configs, RequestMethod requestMethod){
		if(Lists.notEmpty(configs)){
			for(ActionConfig ac: configs){
				UrlMethodMappings mp = ac.getMethodConfigByMethodPath(requestMethod, requestMethodPath);//getRequestMethodPath());
				if(mp != null && this.actionConfig == null){
					this.methodMapping = mp;
					this.actionConfig = ac;
				}
				
				List<UrlInterceptorMethodMapping> temp = ac.getMethodInterceptorByPath(requestMethodPath);
				if(temp != null){
					if(interceptorMappings == null){
						interceptorMappings = new HashMap<>();
					}
					interceptorMappings.put(ac, temp);
				}
			}
		}
	}
	
	/**
	 * Action 是否有返回值
	 * 
	 * @return
	 */
	public boolean hasActionResult() {
		return actionResult != null;
	}

	/**
	 * 是否有对应的映射方法，如果没有通常表示 此 URL 在 MWF 中找不到映射处理方法
	 *
	 * @return
	 */
	public boolean hasMethodMapping(){
		return methodMapping != null;
	}
	
	/**
	 *
	 * @return
	 */
	public String getRequestPath() {
		if(actionConfig != null)
			return actionConfig.getRequestPath();
		return null;
	}

	/**
	 * 与 Method 映射的路径
	 *
	 * @return
	 */
	public String getRequestMethodPath() {
		if(StringUtils.stringIsEmpty(requestMethodPath))
			return "/";
		return this.requestMethodPath;
	}

	/**
	 * 取 @Before 拦截方法
	 *
	 * @return
	 */
	public List<UrlInterceptorMethodMapping> getBeforeInterceptMethod() {
		List<UrlInterceptorMethodMapping> result = Lists.newArrayList();
		if(interceptorMappings != null){
			for(ActionConfig ac: interceptorMappings.keySet()){
				for(UrlInterceptorMethodMapping m: interceptorMappings.get(ac)){
					if(m.isBeforeMethod()){
						m.setActionConfig(ac);
						result.add(m);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 取 @After 拦截方法
	 *
	 * @return
	 */
	public List<UrlInterceptorMethodMapping> getAfterInterceptMethod() {
		List<UrlInterceptorMethodMapping> result = Lists.newArrayList();
		if(interceptorMappings != null){
			for(ActionConfig ac: interceptorMappings.keySet()){
				for(UrlInterceptorMethodMapping m: interceptorMappings.get(ac)){
					if(m.isAfterMethod()){
						m.setActionConfig(ac);
						result.add(m);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 *
	 * @param method
	 */
	public void setUrlMethodMapping(UrlMethodMappings method) {
		this.methodMapping = method;
	}

	public UrlMethodMappings getUrlMethodMapping(){
		return this.methodMapping;
	}
	
	/**
	 *
	 * @return
	 */
	public Object getActionInstance() throws Exception{
		return WebApplicationContext.getActionInstance(actionConfig);
	}

	/**
	 * 参数值数组
	 *
	 * @param args
	 */
	public void setMethodArgs(Object[] args) {
		this.methodArgs = args;
	}


	/**
	 *
	 * @return
	 */
	public Object[] getMethodArgs() {
		return this.methodArgs;
	}
	
	/**
	 *
	 * @param actionResult
	 */
	public void setActionResult(Object actionResult) {
		this.actionResult = actionResult;
	}

	/**
	 * Action 的方法 是否添加了 @ResponseBody 注解
	 *
	 * @return
	 */
	public boolean defineResponseBody() {
		return (methodMapping != null && methodMapping.isResponseBody());
	}

	/**
	 * 判断 ActionResult 是不是一个 String
	 *
	 * @return
	 */
	public boolean actionResultIsString() {
		return (actionResult != null && actionResult instanceof String);
	}

	/**
	 *
	 * @return
	 */
	public String actionResultAsString() {
		return actionResult != null? actionResult.toString(): null;
	}

	/**
	 *
	 * @return
	 */
	public String actionResultAsJson() {
		if(GlobalConfig.isConvertResult2JsonWithNull()){
			return MiscUtils.convert2JsonWithNull(actionResult);
		}
		return MiscUtils.convert2Json(actionResult);
	}

	public Object getActionResult(){
		return this.actionResult;
	}

	/**
	 *
	 * @param forwardResult
	 */
	public void setActionForward(ActionForward forwardResult) {
		this.actionForward = forwardResult;
	}
	
	public ActionForward getActionForward(){
		return this.actionForward;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionExecuteContext [actionConfig=");
		builder.append(actionConfig);
		builder.append(", requestMethodPath=");
		builder.append(requestMethodPath);
		builder.append(", methodMapping=");
		builder.append(methodMapping);
		builder.append(", methodArgs=");
		builder.append(Arrays.toString(methodArgs));
		builder.append(", actionResult=");
		builder.append(actionResult);
		builder.append(", actionForward=");
		builder.append(actionForward);
		builder.append("]");
		return builder.toString();
	}

	public Exception getActionException() {
		return actionException;
	}

	public void setActionException(Exception actionException) {
		this.actionException = actionException;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public boolean isExceptonHandlerHasResponseBody() {
		return exceptonHandlerHasResponseBody;
	}

	public void setExceptonHandlerHasResponseBody(boolean exceptonHandlerHasResponseBody) {
		this.exceptonHandlerHasResponseBody = exceptonHandlerHasResponseBody;
	}

			
}