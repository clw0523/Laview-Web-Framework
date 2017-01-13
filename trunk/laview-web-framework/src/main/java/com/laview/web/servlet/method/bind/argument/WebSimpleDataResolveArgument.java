/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind.argument;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.laview.commons.collections.MapUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.web.RequestContentType;
import com.laview.commons.web.RequestMethod;
import com.laview.commons.web.ServletUtils;
import com.laview.web.servlet.Cookies;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.method.bind.ResolveArgumentContext;
import com.laview.web.servlet.method.bind.WebResolveArgument;
import com.laview.web.servlet.util.MapToObject;
import com.laview.web.servlet.webbean.FieldConfig;
import com.laview.web.servlet.webbean.WebBeanConfig;
import com.laview.web.servlet.webbean.WebBeanConfigs;
import com.laview.web.upload.MultipartRequestUtils;
import com.laview.web.upload.MultipartRequestWrapper;

/**
 * 对使用自定义类作为参数类型的变更的赋值。
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebSimpleDataResolveArgument implements WebResolveArgument{

	private static final Logger log = Logger.getLogger(WebSimpleDataResolveArgument.class);

	/**
	 * 属性设置黑名单：表示名单中的属性是禁止赋值
	 */
	private static final Set<String> blacklist = new HashSet<String>();
	
	private static final WebSimpleDataResolveArgument instance = new WebSimpleDataResolveArgument();
	
	static{
		blacklist.add("class");
	}
	
	public static WebSimpleDataResolveArgument getInstance(){
		return instance;
	}
	
	/**
	 * Pojo
	 * 
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#accept(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public boolean accept(ResolveArgumentContext argumentContext) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#resolveArgument(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception {
		
		ServletData webRequest = argumentContext.getServletData();
		
		Class<?> argType = argumentContext.getParameterType();
		
		String argName = argumentContext.getParameterName();
		
		if(argType == null)
			throw new Exception(" Parameter Type is Null.");
		
		//testUploadFile(webRequest);
		
		if(log.isDebugEnabled()){
			log.info(" *** WebSimpleDataResolveArgument.resolveArgument(), argName=" + argName);
			log.info(" *** Request Class Type:" + webRequest.getRequest().getClass().getCanonicalName());
		}
	
		Object argObject = null;
		
		Map<String, Object> values = null;
		
		/**
		 * 提交上来的数据，是以 JSON 形式组织，就要进行特殊处理。注意：一般来说，都会将整个 JSON 包装成一个对象。所以目前，只支持整个 JSON 就是一个对象的情况
		 */
		if(webRequest.getContentType() == RequestContentType.REQUEST_JSON){
			argObject = extractValuesFromRequestBody(webRequest, argType);
		}
		
		if(argObject == null){
			values = extractValuesFromRequestParameters(webRequest, argName);
			
			//如果是ajax put,post请求，参数数据可能在request body里面		
			//put中文会乱码
			/*if(MapUtils.isNullOrEmpty(values) 
					&& (webRequest.getRequestMethod() == RequestMethod.PUT 
					|| webRequest.getRequestMethod() == RequestMethod.POST)){
				String json = ServletUtils.getRequestJsonBody(webRequest.getRequest());
				values = MapUtils.getMapByUrlParams(json);
			}*/
			
			if(WebBeanConfigs.beanIsWebBean(argType)){
				//TODO: 要处理这里
				getValueForWebBean(webRequest, argType, values);
			}
			
			//创建这个参数类型的对象 (Action 请求处理方法的参数对象)
			argObject = argType.newInstance();
		}
				
		if(values != null && values.size() > 0){

			MapToObject.filledWith(values, argObject);
		}
		
		return argObject;
	}

	/**
	 * 从  Request 的 body （即数据流）中提取数据值。。。例如  POST JSON 数据。。要将 JSON 数据封装成一个大类，而不是抽取其中一些数据
	 *
	 * @param webRequest
	 */
	private Object extractValuesFromRequestBody(ServletData webRequest, Class<?> argType) {
		String json = ServletUtils.getRequestJsonBody(webRequest.getRequest());
		if(StringUtils.stringNotEmpty(json)){
			return JSON.parseObject(json, argType);
		}
		return null;
	}

	/**
	 * 从 Request 的  Parameters 中提取数据值，最终会返回  argName 对象的所有需要设置的属性与属性值的映射。
	 *
	 * @param webRequest
	 * @param argName
	 * @return
	 */
	private Map<String, Object> extractValuesFromRequestParameters(ServletData webRequest, String argName) {
		
		Map<String, Object> values;
		
		/**
		 * 以参数名 argName 为前缀从 Request 中获取   ObjectName.field 形式的数据
		 */
		//上传文件， Request 就会变成： org.apache.struts.upload.MultipartRequestWrapper 类
		if(ReflectUtils.isInheritance(webRequest.getRequest().getClass(), MultipartRequestWrapper.class)){  //带有文件上传的情况
			//由于文件上传时，参数形式有所不同，所以在这里需要专门处理
			Map<String, Object> parameterValues = MultipartRequestUtils.getMultipartRequestParameterValues(webRequest.getRequest(), argName);
			
			//再从 请求参数中分析 出  ObjectName.field 形式数据
			values = MapUtils.getParametersStartingWith(parameterValues, argName);
			
			values = (MapUtils.notEmpty(values)? values: parameterValues);
		}
		else{
			//如果不是文件上传，则直接从 Request 参数中，分析出  ObjectName.field 形式数据
			values = ServletUtils.getParametersStartingWith(webRequest.getRequest(), argName);
		}
		
		if(log.isDebugEnabled())
			log.info(" SimpleData Resolve Argument Values:" + values);
		
		//如果没有  ObjectName.field 形式数据，就直接用  argName 作为参数名称来从  request 中寻找数据
		return (MapUtils.notEmpty(values)? values: ServletUtils.getParameters(webRequest.getRequest()));
	}

	/**
	 * 如果参数是 WebBean 注解类，则根据类的注解配置，从相应的地方获取数据
	 *
	 * @param webRequest
	 * @param argType
	 * @param values
	 */
	private void getValueForWebBean(ServletData webRequest, Class<?> argType, Map<String, Object> values) {
		WebBeanConfig beanConfig = WebBeanConfigs.getWebBeanConfig(argType);
		
		List<FieldConfig> fieldConfigs = beanConfig.getFieldConfigs();
		
		for(FieldConfig fieldConfig: fieldConfigs){
			Object value = null;
			switch(fieldConfig.getValueFrom()){
				case Both:{
					value = webRequest.getSessionValue(fieldConfig.getAttributeName());
					if(value == null){
						value = webRequest.getRequestHeader(fieldConfig.getAttributeName());
					}
					break;
				}
				case Header:{
					value = webRequest.getRequestHeader(fieldConfig.getAttributeName());
					if(value == null)
						value = fieldConfig.getDefaultValue();
					break;
				}
				case Request:{
					break;
				}
				case Session:{
					value = webRequest.getSessionValue(fieldConfig.getAttributeName());
					if(value == null)
						value = fieldConfig.getDefaultValue();
					break;
				}
				case Cookies:{
					if(webRequest instanceof Cookies){
						Cookies cookies = (Cookies)webRequest;
						value = cookies.getCookie(fieldConfig.getAttributeName());
					}
					break;
				}
				default:
					break;
			}
			
			if(value != null){
				//以从客户端提交的数据为主，其他地方的数据为辅
				if(!values.containsKey(fieldConfig.getFieldName())){
					values.put(fieldConfig.getFieldName(), value);
				}
			}
		}
	}

}