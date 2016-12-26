/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.laview.commons.collections.CollectionUtils;
import com.laview.commons.collections.Lists;
import com.laview.commons.lang.Callback;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.reflect.LocalVariableTableParameterNameDiscoverer;
import com.laview.commons.reflect.ParameterNameDiscoverer;
import com.laview.commons.web.RequestMethod;
import com.laview.web.annotation.laview.SessionValue;
import com.laview.web.annotation.springmvc.CookieValue;
import com.laview.web.annotation.springmvc.PathVariable;
import com.laview.web.annotation.springmvc.RequestHeader;
import com.laview.web.annotation.springmvc.ResponseBody;

/**
 * Action 类的一个方法的  URL 与 方法映射表。
 * 
 * 类的单个 方法
 * 
 * 记录 Method 中使用到的信息，如参数，返回值 的注解信息，包括：
 * 
 *  
 *  @PathVaribles
 *  
 *  @RequestHeader 等
 *
 * @author laview_chen
 * @since: v1.0
 */
public class UrlMethodMappings extends UrlMethodMapping implements UrlMethodInfo{

	/**
	 * 方法是否定义了一个 ResponseBody 注解
	 */
	private boolean responseBody;
	
	/**
	 * 在方法上使用 @RequestMapping 定义的请求方法
	 */
	private RequestMethod[] requestMethods = null;
	
	/**
	 * 添加映射
	 *
	 * @param path
	 * @param method
	 * @param requestMethods
	 * @param regexPattern             --- 所映射的路径是否使用了 正则表达式 或者变量
	 */
	public void addMapping(String path, Method method, RequestMethod[] requestMethods, boolean regexPattern) {
		super.addMapping(path, method, regexPattern);
		this.requestMethods = requestMethods;
		
/*		
		}else{
			logger.error("[LWF]==> 映射已经存在，不能存在两个相同的映射！数据[path=" + 
						path + ", method=" + method.getName() + ", requestMethods=" + Arrays.deepToString(requestMethods) + "]");
			throw new RuntimeException("[LWF]==> 映射已经存在，不能存在两个相同的映射！");
		}*/
	}

	/**
	 * 扫描方法，获取方法的详细描述
	 */
	@Override
	public void scanMethodParameter(){
		super.scanMethodParameter();
		
		responseBody = getMethod().isAnnotationPresent(ResponseBody.class);
	}
	
	/**
	 *
	 * @return
	 */
	public RequestMethod[] getRequestMethods() {
		return requestMethods;
	}

	/**
	 *
	 * @param requestMethods
	 */
	public void setRequestMethods(RequestMethod[] requestMethods) {
		this.requestMethods = requestMethods;
	}

	/**
	 * 判断 methods 是否包含在  requestMethods 内
	 *
	 * @param requestMethods2
	 * @return
	 */
	public boolean contains(RequestMethod[] methods) {
		if(requestMethods == null || requestMethods.length == 0)
			return true;

		if(methods == null || methods.length == 0)
			return true;
		
		for(int i=0; i < requestMethods.length; i++){
			for(int j=0; j < methods.length; j++){
				if(requestMethods[i] == methods[j])
					return true;
			}
		}
		return false;
	}

	/**
	 * 判断 requestMethods 是否包含有 requestMethod
	 *
	 * @param requestMethod
	 * @return
	 */
	public boolean contains(RequestMethod requestMethod) {
		if(requestMethods == null || requestMethods.length == 0)  //对所有方法有效，所以一定是 true
			return true;
		
		for(RequestMethod method: requestMethods)
			if(method == requestMethod)
				return true;
		
		return false;
	}
	
//------ 下面是方法参数处理内容 -------------------------------------------	
	
	public boolean isResponseBody(){
		return this.responseBody;
	}
	

}