/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.util;

import java.util.HashMap;

import com.laview.web.servlet.util.MapToObject;

/**
 * Web 请求提交数据存放 Map。
 * 
 * 这个 Map 的优点就是，可以将数据转存到其他的 Entity 中去。一方面，可以以 Map 的方式来操作处理数据，另一方面，也可以转成 Bean
 *
 * @author laview_chen
 * @since: v1.0
 */
public class WebRequestDataMap extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8627861466648355180L;

	/**
	 * 将数据转到实体
	 *
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public <T> T toEntity(T entity) throws Exception{
		MapToObject.filledWith(this, entity);
		return entity;
	}

	/**
	 * 使用实体类类型来进行转换
	 *
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T toEntity(Class<T> clazz) throws Exception{
		T result = clazz.newInstance();
		
		return toEntity(result);
	}
}