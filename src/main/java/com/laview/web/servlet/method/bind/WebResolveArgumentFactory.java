/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.laview.web.servlet.ServletData;
import com.laview.web.servlet.action.config.UrlMethodMapping;
import com.laview.web.servlet.method.bind.argument.BaseTypeResolveArgument;
import com.laview.web.servlet.method.bind.argument.CommonTypeResolveArgument;
import com.laview.web.servlet.method.bind.argument.WebSimpleDataResolveArgument;
import com.laview.web.servlet.method.bind.argument.WebStandardTypeResolveArgument;


/**
 * 将 Request 中的数据类型转换成 Annotation 方法的参数类型的转换工厂。
 * 
 * 例如， 方法中需要注入一个 int 类型数据，则会将 Request 中对应的字符串值转成 int，然后注入。
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebResolveArgumentFactory {

	private final static Logger logger = Logger.getLogger(WebResolveArgumentFactory.class);
	
	private final static List<WebResolveArgument> resolves = new ArrayList<WebResolveArgument>(){{
		
		//通用类型 （日期、上传文件、十进制大数字类等的复合类组装）
		add(new CommonTypeResolveArgument());
		
		//基础数据类型（数值类型、字符串类型等）
		add(new BaseTypeResolveArgument());
		
		//Web Servlet 使用类的组装注入（如 Request, Response, InputStream , HttpSession 等等 Servlet 所使用的类）
		add(new WebStandardTypeResolveArgument());
		
		// Pojo 类的组装 （即由用户自定义的数据类的组装）
		//add(new WebSimpleDataResolveArgument());
	}};
	
	/**
	 * 数据组装
	 *
	 * @param servletData
	 * @param methodMapping
	 * @param parameterType
	 * @param parameterName
	 * @return
	 */
	public static Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception{
		//逐个参数进行组装
		for(WebResolveArgument resolve: resolves){
	
			if(resolve.accept(argumentContext)){
				return resolve.resolveArgument(argumentContext);
			}
		}
		
		//如果不是标准类，就当是 Pojo 来组装
		return WebSimpleDataResolveArgument.getInstance().resolveArgument(argumentContext);
	}
}