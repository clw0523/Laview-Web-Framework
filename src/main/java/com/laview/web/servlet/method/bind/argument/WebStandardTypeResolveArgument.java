/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind.argument;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.laview.commons.lang.ReflectUtils;
import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.method.bind.ResolveArgumentContext;
import com.laview.web.servlet.method.bind.WebResolveArgument;


/**
 * 将 Request和 Response 中的流类(inputStream outputStream 等等)注入到 Annotation 方法中
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebStandardTypeResolveArgument implements WebResolveArgument {

	private static final Logger logger = Logger.getLogger(WebStandardTypeResolveArgument.class);
	
	private static final Set<Class<?>> handlerTypes = new HashSet<Class<?>>(){{
		add(HttpServletRequest.class);
		add(HttpServletResponse.class);
		add(HttpSession.class);
		add(Reader.class);
		add(InputStream.class);
		add(OutputStream.class);
		add(Writer.class);
		add(PrintWriter.class);
	}};
	

	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#accept(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public boolean accept(ResolveArgumentContext argumentContext) {
		return handlerTypes.contains(argumentContext.getParameterType());
	}
	
	/* (non-Javadoc)
	 * @see com.laview.web.servlet.method.bind.WebResolveArgument#resolveArgument(com.laview.web.servlet.method.bind.ResolveArgumentContext)
	 */
	@Override
	public Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception {
		logger.debug("[LWF ResolveArgument]==> 处理 Request 和 Response 本身及流类的注入装填");
		
		ServletData webRequest = argumentContext.getServletData();
		Class<?> argType = argumentContext.getParameterType();
		if(ReflectUtils.isInheritance(argType, HttpServletRequest.class)){
			return webRequest.getRequest();
		}

		if(ReflectUtils.isInheritance(argType, HttpServletResponse.class)){
			return webRequest.getResponse();
		}

		if(ReflectUtils.isInheritance(argType, HttpSession.class)){
			return webRequest.getRequest().getSession();
		}

		if(ReflectUtils.isInheritance(argType, Reader.class))
			return webRequest.getRequest().getReader();

		if(ReflectUtils.isInheritance(argType, InputStream.class))
			return webRequest.getRequest().getInputStream();

		if(ReflectUtils.isInheritance(argType, OutputStream.class))
			return webRequest.getResponse().getOutputStream();

		if(ReflectUtils.isInheritance(argType, Writer.class))
			return webRequest.getResponse().getWriter();
		
		if(ReflectUtils.isInheritance(argType, PrintWriter.class)){
			return webRequest.getResponse().getWriter();
		}

		return WebResolveArgument.UNRESOLVED;
	}
}