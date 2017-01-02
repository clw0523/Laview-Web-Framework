/**
 * @Copyright:Copyright (c) 1991 - 2013
 * @Company: Laview
 */
package com.laview.web.servlet.http;

import java.util.HashMap;
import java.util.Map;

import com.laview.commons.web.path.PathPair;
import com.laview.web.servlet.ServletData;

/**
 * 处理静态文件的映射，在这里使用 "/static" 作为静态资源的默认路径
 *
 * @author laview_chen
 * @since: v1.0
 */
public class SimpleUrlHandlerMapping{

	/**
	 * <资源请求，本地目录>
	 */
	private Map<String, String> staticResourceMapping = new HashMap<String, String>(){{
		put("/static", "/static");
	}}; 
	
	/**
	 * 获取 静态资源映射 本地路径
	 *
	 * @param servletData
	 * @return
	 */
	public PathPair findStaticResourceMappingPath(ServletData servletData){
		String path = servletData.getRequestPath();
		
		Iterable<PathPair> pp = new PathPair(path);
		for(PathPair p: pp){
			if(staticResourceMapping.containsKey(p.getFrontPath())){
				return PathPair.createInstance(staticResourceMapping.get(p.getFrontPath()), p.getLaterPath());
			}
		}
		return null;
	}
	
	public void addStaticResourceMapping(String key,String value){
		staticResourceMapping.put(key, value);
	}

}