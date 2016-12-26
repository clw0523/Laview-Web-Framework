/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.method.bind;


/**
 * Annotation 中的参数绑定接口，用来将 Request 中的数据转成参数
 *
 * @author laview_chen
 * @since: v1.0
 */
public interface WebResolveArgument {

	public static final Object UNRESOLVED = new Object();

	/**
	 * 转换参数方法
	 *
	 * @param argumentContext   包装了上下文信息
	 * @return			          返回  UNRESOLVED 表示不是本方法的转换，其他值（包括null）表示本方法接受并进行了转换
	 * @throws Exception
	 */
	public Object resolveArgument(ResolveArgumentContext argumentContext) throws Exception;
	
	/**
	 * 是否接受这个参数的处理。
	 *
	 * @param argumentContext
	 * @return
	 */
	public boolean accept(ResolveArgumentContext argumentContext);
}