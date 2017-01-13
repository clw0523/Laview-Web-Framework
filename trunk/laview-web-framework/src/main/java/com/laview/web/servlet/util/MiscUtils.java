/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.util;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 *
 *
 * @author laview_chen
 * @since: v1.0
 */
public class MiscUtils {

	/**
	 * 将一个 Object 转化为  Json
	 *
	 * @param content
	 * @return
	 */
	public static String convert2Json(Object content){
		SerializeConfig mapping = new SerializeConfig();
		
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		return JSON.toJSONString(content, mapping);		
	}
	
	/**
	 * 将一个 Object 转化为  Json,带null
	 *
	 * @param content
	 * @return
	 */
	public static String convert2JsonWithNull(Object content){
		return JSON.toJSONStringWithDateFormat(content, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
	}
	
}