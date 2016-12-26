/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.laview.commons.collections.CollectionUtils;
import com.laview.commons.collections.Lists;
import com.laview.commons.lang.Callback;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.reflect.LocalVariableTableParameterNameDiscoverer;
import com.laview.commons.reflect.ParameterNameDiscoverer;
import com.laview.web.annotation.laview.SessionValue;
import com.laview.web.annotation.springmvc.CookieValue;
import com.laview.web.annotation.springmvc.PathVariable;
import com.laview.web.annotation.springmvc.RequestHeader;


/**
 * 记录了 URL 与 Method 的映射，这是一个基类
 *
 * @author laview_chen
 * @since: v1.0
 */
public abstract class UrlMethodMapping{
	
	/**
	 * URL 是不是一个正则表达式，或者带变量的 
	 */
	private boolean regexPattern = false;
	
	private Method method;
	
	/**
	 * method 上定义的 path
	 */
	private String path;
	
	/**
	 * HandlerMethodArgumentDescriptor 方法的参数描述数组 <参数名，参数详细描述对象>
	 * 注意提供顺序 --- 使用 LinkedHashMap 来保证参数出现的次序
	 */
	private Map<String, HandlerMethodArgumentDescriptor> parameterDescripts = 
				new LinkedHashMap<String, HandlerMethodArgumentDescriptor>();
	
	private List<String> parameterNameList;
	
	/**
	 * 添加参数的值
	 *
	 * @param path
	 * @param method
	 * @param regexPattern
	 */
	public void addMapping(String path, Method method, boolean regexPattern) {
		this.path = path;
		
		this.method = method;
		
		this.regexPattern = regexPattern;
		
			//扫描方法的参数
		scanMethodParameter();
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return this.path;
	}
	
	/**
	 * 获得方法 第 index 个参数的名称
	 *
	 * @param i
	 * @return
	 */
	public String getParameterNameBy(int index){
		return parameterNameList.get(index);
	}
	
	public void setMethod(Method method){
		this.method = method;
	}
	
	public Method getMethod(){
		return this.method;
	}
	
	/**
	 * @return the regexPattern
	 */
	public boolean isRegexPattern() {
		return regexPattern;
	}

	/**
	 * @param regexPattern the regexPattern to set
	 */
	public void setRegexPattern(boolean regexPattern) {
		this.regexPattern = regexPattern;
	}

	/**
	 * 获取参数中 PathVariable 的索引
	 *
	 * @return
	 */
	public HandlerMethodArgumentDescriptor[] getPathVariableIndexs(){
		final List<HandlerMethodArgumentDescriptor> results = Lists.newArrayList();
		CollectionUtils.each(parameterDescripts.values(), new Callback<HandlerMethodArgumentDescriptor>(){

			@Override
			public void doWith(HandlerMethodArgumentDescriptor t) {
				if(t.isPathVariable())
					results.add(t);
			}
			
		});
		
		return results.toArray(new HandlerMethodArgumentDescriptor[results.size()]);
	}
	
	/**
	 * 判断是否有参数
	 * @return
	 */
	public boolean hasMethodArguments() {
		return Lists.notEmpty(parameterNameList);
	}

	
	/**
	 * 扫描方法参数，对方法参数进行 识别（添加特别注解的参数）和处理
	 */
	public void scanMethodParameter(){
		Class<?>[] parameterTypes = method.getParameterTypes();
		String[] parameterNames = getMethodParameters();
		if(StringUtils.stringArrayNotEmpty(parameterNames)){
			parameterNameList = Arrays.asList(parameterNames);
		}
		Annotation[][] annotations = method.getParameterAnnotations();
		
		//按照参数顺序插入到映射表
		for(int i=0; i < parameterNames.length; i++){
			HandlerMethodArgumentDescriptor descriptor = new HandlerMethodArgumentDescriptor();
			descriptor.setParameterName(parameterNames[i]);
			descriptor.setParameterType(parameterTypes[i]);
			
			if(annotations[i].length > 0){ //对参数上的注解进行处理
				for(Annotation annotation: annotations[i]){ 
					if(annotation instanceof PathVariable){
						descriptor.setPathVariable(true);
					}else if(annotation instanceof RequestHeader){
						descriptor.setRequestHeader((RequestHeader)annotation);
					}else if(annotation instanceof CookieValue){
						descriptor.setCookieValue((CookieValue)annotation);
					}else if(annotation instanceof SessionValue){
						descriptor.setSessionValue((SessionValue)annotation);
					}
				}
			}
			
			descriptor.setParameterIndex(i);
			
			parameterDescripts.put(parameterNames[i], descriptor);
		}
	}
	
	/**
	 * 获取第 parameterIndex 个参数上的，某个 annotation 注解，如果没有则返回 null;
	 *
	 * @param parameterIndex
	 * @param annotation
	 * @return
	 */
	public RequestHeader getRequestHeaderAnnotation(String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null){
			return descriptor.getRequestHeader();
		}
		return null;
	}

	/**
	 *
	 * @param parameterName
	 * @return
	 */
	public CookieValue getCookieValueAnnotation(String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null){
			return descriptor.getCookieValue();
		}
		return null;
	}

	/**
	 *
	 * @param parameterName
	 * @return
	 */
	public SessionValue getSessionValueAnnotation(String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null){
			return descriptor.getSessionValue();
		}
		return null;
	}
	
	/**
	 * 获取方法所有参数名称
	 *
	 * @return
	 */
	private String[] getMethodParameters(){
		//如果方法有参数，就要组织并注入参数
		ParameterNameDiscoverer pnd = new LocalVariableTableParameterNameDiscoverer();
		return pnd.getParameterNames(method);
	}
	
	/**
	 * 判断参数是否添加 @PathVariable 注解
	 * 
	 * @param parameterType 
	 * @param parameterName
	 * @return
	 */
	public boolean checkIsPathVariable(Class<?> parameterType, String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null && descriptor.getParameterName().equals(parameterName) && descriptor.isPathVariable())
			return true;
		return false;
	}

	/**
	 * 判断参数是否添加 @RequestHeader 注解
	 * 
	 * @param parameterType 
	 * @param parameterName
	 * @return
	 */
	public boolean checkIsRequestHeader(Class<?> parameterType, String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null && descriptor.getParameterName().equals(parameterName) && descriptor.isRequestHeader())
			return true;
		return false;
	}

	/**
	 *
	 * @param class1
	 * @param string
	 * @return
	 */
	public boolean checkIsCookieValue(Class<?> parameterType, String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null && descriptor.getParameterName().equals(parameterName) && descriptor.isCookieValue())
			return true;
		return false;
	}

	/**
	 * 参数是否有 SessionValue 注解
	 *
	 * @param parameterType
	 * @param parameterName
	 * @return
	 */
	public boolean checkIsSessionValue(Class<?> parameterType, String parameterName) {
		HandlerMethodArgumentDescriptor descriptor = parameterDescripts.get(parameterName);
		if(descriptor != null && descriptor.getParameterName().equals(parameterName) && descriptor.isSessionValue())
			return true;
		return false;
	}
	
}