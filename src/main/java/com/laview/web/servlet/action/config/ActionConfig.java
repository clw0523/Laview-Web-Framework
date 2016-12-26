/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.laview.commons.collections.Lists;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.RequestMethod;
import com.laview.commons.web.path.AntPathMatcher;
import com.laview.commons.web.path.PathMatcher;
import com.laview.container.core.BeanInfo;
import com.laview.container.core.BeanInfo.BeanCreatePolicy;
import com.laview.web.annotation.laview.Before;
import com.laview.web.servlet.action.config.UrlInterceptorMethodMapping.InterceptorMethodType;

/**
 * 类的注解配置类，这是 Action 类级的 信息，通常每一个类对应一个注解配置
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ActionConfig {

	private final static Logger logger = Logger.getLogger(ActionConfig.class);
	
	/**
	 * URL <---> Action Method 的映射
	 */
	private Map<UrlMethodKey, UrlMethodMappings> mappings = new HashMap<UrlMethodKey, UrlMethodMappings>();
	
	/**
	 * 支持 Regx (正则表达式)
	 */
	private Map<UrlMethodKey, UrlMethodMappings> regxMappings = new HashMap<UrlMethodKey, UrlMethodMappings>();

	/**
	 * 此 Action 所包含的拦截方法配置  <请求路径, mappings>
	 */
	private Map<String, List<UrlInterceptorMethodMapping>> interMethodMappings;
	
	/**
	 * 用来匹配 URL
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * 这只是配合  @Controller 或  Structs1 的内容，在将来版本中有可能会去掉
	 */
	private String actionId;
	
	/**
	 * 请求路径
	 */
	private String requestPath;
	
	/**
	 * Action 类名
	 */
	private String className;
	
	/**
	 * 请求处理类的 BeanInfo
	 */
	private BeanInfo actionBeanInfo;
	
	/**
	 * @param path
	 */
	public ActionConfig(String path) {
		this.requestPath = path;
	}
	
	/**
	 * 添加 @before 注解拦截器
	 * 
	 * @param method
	 * @param before
	 */
	public void addInterceptorMethod(Method method, String path, InterceptorMethodType type) {
		if(type == null || StringUtils.stringIsEmpty(path)){
			return ;
		}
		
		if(interMethodMappings == null){
			interMethodMappings = new HashMap<>();
		}
		
		List<UrlInterceptorMethodMapping> mappings = interMethodMappings.get(path);
		if(mappings == null){
			mappings = Lists.newArrayList();
			interMethodMappings.put(path, mappings);
		}
		UrlInterceptorMethodMapping mapping = new UrlInterceptorMethodMapping();
		mapping.addMapping(path, method, false);
		mapping.setMetodType(type);
		
		mappings.add(mapping);
	}
	
	/**
	 * 定义一个  <RequestMethod + url, actionClass.method> 的映射
	 * 
	 * 同一个 url,由于  RequestMethod 不同，看作不同的请求内容
	 *
	 * @param path
	 * @param method
	 * @param requestMethods
	 */
	public void addMethodMapping(String path, Method method, RequestMethod[] requestMethods) {
		
		UrlMethodKey mapKey = new UrlMethodKey(path, requestMethods);

		UrlMethodMappings mapping = findMappingsBy(mapKey);
		
		boolean regexPattern = isRegexOrPathVar(path);
		
		if(mapping == null){
			mapping = new UrlMethodMappings();
			
			if(regexPattern){
				regxMappings.put(mapKey, mapping);
			}else{			
				mappings.put(mapKey, mapping);
			}
		}
		
		mapping.addMapping(path, method, requestMethods, regexPattern);
		
		logger.debug("[请求数]========> requestPath:" + requestPath  + (mappings.size() + regxMappings.size()));
	}

	/**
	 * 查找这个 path 是否已经注册了映射方法
	 *
	 * @param path
	 * @return
	 */
	private UrlMethodMappings findMappingsBy(UrlMethodKey mapKey){
		
		UrlMethodMappings mapping = mappings.get(mapKey);
		
		if(mapping == null){
			
			List<String> matchingPatterns = Lists.newArrayList();
			for(UrlMethodKey key: regxMappings.keySet()){
				if(key.methodsContainer(mapKey) && pathMatcher.match(key.getUrl(), mapKey.getUrl())){
					matchingPatterns.add(key.getUrl());
				}
			}
			
			//从众多的匹配者中，得到最优者
			String bestPatternMatch = null;
			Comparator<String> patternComparator = pathMatcher.getPatternComparator(mapKey.getUrl());
			if (!matchingPatterns.isEmpty()) {
				Collections.sort(matchingPatterns, patternComparator);
				if (logger.isDebugEnabled()) {
					logger.debug("Matching patterns for request [" + mapKey.getUrl() + "] are " + matchingPatterns);
				}
				bestPatternMatch = matchingPatterns.get(0);
			}
			
			if(bestPatternMatch != null){
				return regxMappings.get(new UrlMethodKey(bestPatternMatch, mapKey.getRequestMethods()));
			}
		}
		
		return mapping;
	}
	

	/**
	 * 判断所定义的路么是否为正则表达式等
	 *
	 * @param path
	 * @return
	 */
	private boolean isRegexOrPathVar(String path) {
		boolean result = pathMatcher.isPattern(path);
		if(!result){
			String[] ss = path.split("/");
			if(ss != null){
				for(String s: ss){
					if(s.startsWith("{")&& s.endsWith("}")){
						return true;
					}
				}
			}
		}
		return result;
	}
		
	/**
	 * 取  满足 <path, requestMethod>  (如    </index, Get> ) 的 注解方法配置
	 *
	 * @param requestMethod 
	 * @param requestMethodPath
	 * @return
	 */
	public UrlMethodMappings getMethodConfigByMethodPath(RequestMethod requestMethod, String methodPath) {
		if(logger.isDebugEnabled()){
			logger.debug("[LWF]==> 本类所有映射" + mappings.toString());
			logger.debug("[LWF]==> 获取 URL Method Mapping，methodPath[" + methodPath + "]");
		}
		
		UrlMethodMappings ms = findMappingsBy(new UrlMethodKey(methodPath, requestMethod));
		if(ms != null){
			return ms;
		}else{
			logger.debug("[LWF]==> 找不到对应的 Action 方法，参数：[methodPath=" + methodPath + ", Method=" + requestMethod + "]");
		}
		return null;
	}
	
	/**
	 *
	 * @param path
	 * @return
	 */
	public List<UrlInterceptorMethodMapping> getMethodInterceptorByPath(String path) {
		List<UrlInterceptorMethodMapping> result = null;
		if(interMethodMappings != null){
			result = interMethodMappings.get(path);
			
			if(result == null){
				result = Lists.newArrayList();
				for(String pattern: interMethodMappings.keySet()){
					if(pathMatcher.match(pattern, path)){
						result.addAll(interMethodMappings.get(pattern));
					}
				}
			}
			
		}
		
		return result;
	}

	/**
	 *
	 * @return
	 */
	public String getRequestPath() {
		return this.requestPath;
	}

	/**
	 *
	 * @return
	 */
	public String getType() {
		return this.className;
	}

	/**
	 *
	 * @param canonicalName
	 */
	public void setType(String className) {
		this.className = className;
	}

	/**
	 *
	 * @return
	 */
	public String getActionId() {
		return this.actionId;
	}

	/**
	 *
	 * @param canonicalName
	 */
	public void setActionId(String id) {
		this.actionId = id;
	}

	/**
	 * 顺便将 Bean 设置为单例
	 *
	 * @param theBeanInfo
	 */
	public void setBeanInfo(BeanInfo theBeanInfo) {
		this.actionBeanInfo = theBeanInfo;
		if(actionBeanInfo != null){
			actionBeanInfo.setPolicy(BeanCreatePolicy.SINGLETON);
		}
	}

	public BeanInfo getBeanInfo(){
		return this.actionBeanInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActionConfig [mappings=");
		builder.append(mappings);
		builder.append(", actionId=");
		builder.append(actionId);
		builder.append(", requestPath=");
		builder.append(requestPath);
		builder.append(", className=");
		builder.append(className);
		builder.append(", actionBeanInfo=");
		builder.append(actionBeanInfo);
		builder.append("]");
		return builder.toString();
	}

}