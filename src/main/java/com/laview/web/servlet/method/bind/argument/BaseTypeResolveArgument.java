/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind.argument;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.laview.commons.convert.ConversionServiceUtils;
import com.laview.commons.lang.ReflectUtils;
import com.laview.commons.lang.StringUtils;
import com.laview.commons.lang.ViewBoundHelper;
import com.laview.web.servlet.method.bind.ResolveArgumentContext;
import com.laview.web.servlet.method.bind.WebResolveArgument;


/**
 * 将 Request 中的 字符串值转成 Java 基本类型值，例如将 String 转成 int, long, double等值
 *
 * @author laview_chen
 * @since: v1.0
 */
public class BaseTypeResolveArgument implements WebResolveArgument {

	private static final Logger log = Logger.getLogger(BaseTypeResolveArgument.class);
	
	private static final Map<Class<?>, Class<?>> baseTypes = new HashMap<Class<?>, Class<?>>(){{
		
		put(int.class, Integer.class);
		put(Integer.class, Integer.class);
		put(long.class, Long.class);
		put(Long.class, Long.class);
		put(double.class, Double.class);
		put(Double.class, Double.class);
		put(boolean.class, Boolean.class);
		put(Boolean.class, Boolean.class);
		
		put(float.class, Float.class);
		put(Float.class, Float.class);
		put(byte.class, Byte.class);
		put(Byte.class, Byte.class);
		put(short.class, Short.class);
		put(Short.class, Short.class);
		put(String.class, String.class);
		put(Date.class, Date.class);
	}};
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#accept(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public boolean accept(ResolveArgumentContext argumentContext) {
		return baseTypes.containsKey(argumentContext.getParameterType());
	}

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#resolveArgument(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception {
		log.debug("[LWF ResolveArgument]==> 处理基本类型的参数");
		
		Object submitValue = argumentContext.getSubmitValue();
		if(submitValue == null)
			return null;
		
		String value = submitValue.toString();
		
		//字符串
		if(ReflectUtils.isInheritance(argumentContext.getParameterType(), String.class)){
			return value;
		}

		//日期
		if(ReflectUtils.isInheritance(argumentContext.getParameterType(), Date.class)){
			return ConversionServiceUtils.convert(value, Date.class);
		}
		
		//数值类型
		if(StringUtils.stringNotEmpty(value)){
			Class<?> targetClass = baseTypes.get(argumentContext.getParameterType());
			BaseTypeValueBound bound = (BaseTypeValueBound)ViewBoundHelper.asIs(targetClass, BaseTypeValueBound.class);
			return bound.valueOf(value);
		}else
			return null;
	}

}