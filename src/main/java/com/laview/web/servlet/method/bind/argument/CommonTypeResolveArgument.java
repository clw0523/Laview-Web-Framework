/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind.argument;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.laview.commons.collections.MapUtils;
import com.laview.commons.convert.ConversionService;
import com.laview.commons.convert.ConversionServiceFactory;
import com.laview.commons.convert.ConverterFactory;
import com.laview.commons.lang.ClassUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.method.bind.ResolveArgumentContext;
import com.laview.web.servlet.method.bind.WebResolveArgument;
import com.laview.web.upload.FormFile;
import com.laview.web.upload.MultipartRequestUtils;
import com.laview.web.upload.MultipartRequestWrapper;


/**
 * 由 Java 类库定义的复合类转换器，例如 枚举、数组、精度类（BigDecimal）、还有就是 FormFile (上传文件)
 *
 * @author laview_chen
 * @since: v1.0
 */
public class CommonTypeResolveArgument implements WebResolveArgument{

	private static final Logger log = Logger.getLogger(CommonTypeResolveArgument.class);
	
	private ConversionService conversion = ConversionServiceFactory.createDefaultConversionService();
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#accept(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public boolean accept(ResolveArgumentContext argumentContext) {
		Class<?> argType = argumentContext.getParameterType();
		
		boolean result = ReflectUtils.isInheritance(argType, BigDecimal.class) || 
							argType.isArray() || argType.isEnum() 
							|| ReflectUtils.isInheritance(argType, FormFile.class)
							|| ReflectUtils.isInheritance(argType, Map.class);
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#resolveArgument(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception {
		log.debug("[LWF ResolveArgument]==> 处理 Java 类库中的参数，包括：数组、枚举、FormFile和 BigDecimal");
		
		Class<?> argType = argumentContext.getParameterType();
		ServletData webRequest = argumentContext.getServletData();
		if(ReflectUtils.isInheritance(argType, BigDecimal.class)){
			Object value = argumentContext.getSubmitValue();
			if(value instanceof BigDecimal){
				return value;
			}else{
				return new BigDecimal(value.toString());
			}
		}

		if(argType.isArray()){
			String[] values = webRequest.getRequestParameters(argumentContext.getParameterName());

			if(values != null){
				return conversion.convert(values, argType);
			}
			
			return values;
		}
		
		if(argType.isEnum()){
    		
			Object value = argumentContext.getSubmitValue();
    		if(value != null && value.getClass().isEnum()){
    			return value;
    		}
    		
    		String strValue = value.toString();
    		if(strValue != null && strValue.trim().length() > 0){
    			return ClassUtils.getEnumBy(argType, strValue);
    		}
		}

		if(ReflectUtils.isInheritance(argType, FormFile.class)){
			HttpServletRequest request = webRequest.getRequest();
			if(ReflectUtils.isInheritance(request.getClass(), MultipartRequestWrapper.class)){
				Map<String, Object> parameterValues = MultipartRequestUtils.getMultipartRequestParameterValues(request, argumentContext.getParameterName());
				return parameterValues.get(argumentContext.getParameterName());
			}
		}
		
		/**
		 * 将数据放入到一个 Map 中
		 */
		if(ReflectUtils.isInheritance(argType, Map.class)){
			HttpServletRequest request = webRequest.getRequest();

			Map result = null;
			if(argType.equals(Map.class)){
				result = MapUtils.newSimpleMap();
			}else{
				result = (Map)argType.newInstance();
			}
			if(result != null){
				result.putAll(request.getParameterMap());
			}
			return result;
		}
		
		return WebResolveArgument.UNRESOLVED;
	}

}