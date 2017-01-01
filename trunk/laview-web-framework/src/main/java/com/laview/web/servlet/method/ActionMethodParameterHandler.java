/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.laview.commons.lang.StringUtils;
import com.laview.commons.reflect.LocalVariableTableParameterNameDiscoverer;
import com.laview.commons.reflect.ParameterNameDiscoverer;
import com.laview.commons.web.path.AntPathMatcher;
import com.laview.commons.web.path.PathMatcher;
import com.laview.web.annotation.laview.SessionValue;
import com.laview.web.annotation.springmvc.CookieValue;
import com.laview.web.annotation.springmvc.RequestHeader;
import com.laview.web.servlet.Cookies;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.ActionExecuteContext;
import com.laview.web.servlet.action.config.UrlMethodMapping;
import com.laview.web.servlet.action.config.UrlMethodMappings;
import com.laview.web.servlet.method.bind.ResolveArgumentContext;
import com.laview.web.servlet.method.bind.WebResolveArgumentFactory;

/**
 * Action 请求处理方法的参数注入与装填
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionMethodParameterHandler {

	private final static Logger logger = Logger.getLogger(ActionMethodParameterHandler.class);
	
	/**
	 * 封装请求数据
	 */
	private final ServletData servletData;
	
	/**
	 * @param servletData
	 */
	public ActionMethodParameterHandler(ServletData servletData) {
		this.servletData = servletData;
	}

	/**
	 * 组建方法参数。 遍历方法的所有参数，并能过  WebResolveArgumentFactory 来将不同参数类型 交由 不同的组装类来装配
	 * 
	 * @param method
	 * @return
	 */
	public Object[] resolveHandlerArgument(ActionExecuteContext actionContext) throws Exception{
		
		//获取方法所有参数类型
		UrlMethodMappings methodMapping = actionContext.getUrlMethodMapping();
		Method method = methodMapping.getMethod();
		Class<?>[] parameterTypes = method.getParameterTypes();

		//记录最终的参数值
		List<Object> parameters = new ArrayList<Object>();
		
		if(parameterTypes != null && parameterTypes.length > 0){
			
			//如果方法有参数，就要组织并注入参数
			ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
			String[] parameterNames = pnd.getParameterNames(method); 
			
			//从 URL 中获取参数值
			Map<String, String> pathVarValues = getPathVarValues(actionContext, methodMapping);
			
			for(int i=0; i < parameterTypes.length; i++){
				ResolveArgumentContext context = ResolveArgumentContext.createBy( servletData, methodMapping, parameterTypes[i], parameterNames[i]);
				context.setRequestMethodPath(actionContext.getRequestMethodPath());
				
				//参数值来自于其他地方，如 URL 或 RequestHeader
				if(methodMapping.checkIsPathVariable(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(pathVarValues.get(parameterNames[i]));
				}else if(methodMapping.checkIsRequestHeader(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getRequestHeaderValue(parameterNames[i], methodMapping));
				}else if(methodMapping.checkIsCookieValue(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getCookieValueFrom(parameterNames[i], methodMapping));
				}else if(methodMapping.checkIsSessionValue(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getSessionValueFrom(parameterNames[i], methodMapping));
				}
				
				//从 请求中获取 名称 parameterNames[i] 的值，并转换成 parameterTypes[i] 类型返回--- result 就是参数值
				Object result = WebResolveArgumentFactory.resolveArgument(context);
				
				parameters.add(result);
			}
		}
		
		return parameters.toArray(new Object[parameters.size()]);
	}
	
	/**
	 * 对指定方法Method 组建方法参数。 遍历方法的所有参数，并能过  WebResolveArgumentFactory 来将不同参数类型 交由 不同的组装类来装配
	 * 
	 * @param method
	 * @return
	 */
	public Object[] resolveHandlerArgument(ActionExecuteContext actionContext,Method method) throws Exception{

		//获取方法所有参数类型
		UrlMethodMappings methodMapping = actionContext.getUrlMethodMapping();
		//Method method = methodMapping.getMethod();
		Class<?>[] parameterTypes = method.getParameterTypes();

		//记录最终的参数值
		List<Object> parameters = new ArrayList<Object>();
		
		if(parameterTypes != null && parameterTypes.length > 0){
			
			//如果方法有参数，就要组织并注入参数
			ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
			String[] parameterNames = pnd.getParameterNames(method); 
			
			//从 URL 中获取参数值
			Map<String, String> pathVarValues = getPathVarValues(actionContext, methodMapping);
			
			for(int i=0; i < parameterTypes.length; i++){
				ResolveArgumentContext context = ResolveArgumentContext.createBy( servletData, methodMapping, parameterTypes[i], parameterNames[i]);
				context.setRequestMethodPath(actionContext.getRequestMethodPath());
				
				//参数值来自于其他地方，如 URL 或 RequestHeader
				if(methodMapping.checkIsPathVariable(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(pathVarValues.get(parameterNames[i]));
				}else if(methodMapping.checkIsRequestHeader(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getRequestHeaderValue(parameterNames[i], methodMapping));
				}else if(methodMapping.checkIsCookieValue(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getCookieValueFrom(parameterNames[i], methodMapping));
				}else if(methodMapping.checkIsSessionValue(parameterTypes[i], parameterNames[i])){
					context.setSourceValue(getSessionValueFrom(parameterNames[i], methodMapping));
				}
				
				//从 请求中获取 名称 parameterNames[i] 的值，并转换成 parameterTypes[i] 类型返回--- result 就是参数值
				Object result = WebResolveArgumentFactory.resolveArgument(context);
				
				parameters.add(result);
			}
		}
		
		return parameters.toArray(new Object[parameters.size()]);
	}

	/**
	 * 如果包含 URL 参数，就从 URL 中得到参数值
	 *
	 * @param actionContext
	 * @param methodMapping
	 */
	private Map<String, String> getPathVarValues(ActionExecuteContext actionContext, UrlMethodMappings methodMapping) {
		if(methodMapping.isRegexPattern()){
			PathMatcher matcher = new AntPathMatcher();

			return matcher.extractUriTemplateVariables(methodMapping.getPath(), actionContext.getRequestMethodPath());
		}
		
		return null;
	}	
	
	/**
	 * 从 请求头中获取参数值
	 *
	 * @param parameterName
	 * @param methodMapping
	 * @return
	 * @throws Exception
	 */
	private String getRequestHeaderValue(String parameterName, UrlMethodMappings methodMapping) throws Exception{
		RequestHeader rh = methodMapping.getRequestHeaderAnnotation(parameterName);
		String headerName = StringUtils.stringNotEmpty(rh.value())?rh.value(): parameterName;
		String tmpResult = servletData.getRequest().getHeader(headerName);
		if(tmpResult == null){
			if(rh.required()){
				throw new Exception("[LWF resolveHandlerArgument]==> 请求头[" + rh.value() + "]不存在！");
			}
			tmpResult = rh.defaultValue();
		}
		return tmpResult;
	}
	
	/**
	 * 从 Request 中获取 Cookie 的值
	 *
	 * @param actionContext
	 * @return
	 */
	private String getCookieValueFrom(String parameterName, UrlMethodMappings methodMapping) {
		CookieValue cv = methodMapping.getCookieValueAnnotation(parameterName);
		if(servletData instanceof Cookies){
			Cookies cookies = (Cookies)servletData;
			return cookies.getCookie( StringUtils.stringIsEmpty(cv.value())?parameterName: cv.value());
		}
		return null;
	}
	
	/**
	 *
	 * @param string
	 * @param methodMapping
	 * @return
	 */
	private Object getSessionValueFrom(String parameterName, UrlMethodMappings methodMapping) {
		SessionValue sv = methodMapping.getSessionValueAnnotation(parameterName);
		String sessionName = StringUtils.stringNotEmpty(sv.value())?sv.value(): parameterName;
		Object result = servletData.getSessionValue(sessionName);
		
		return result != null?result: sv.defaultValue();
	}

}