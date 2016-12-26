/**
 * @Copyright:Copyright (c) 1991 - 2012
 * @Company: Laview
 */
package com.laview.web.servlet.view;

import java.util.HashMap;
import java.util.Map;

import com.laview.commons.lang.StringUtils;

/**
 * 返回渲染 的视图和数据
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ModelAndView {

	private String view;
	
	private Map<String, Object> datas = new HashMap<String, Object>();
	
	public ModelAndView(){}
	
	public ModelAndView(String view){
		this.view = view;
	}
	
	public ModelAndView(String view, String key, Object value){
		this.view = view;
		addObject(key, value);
	}
	
	public ModelAndView(String view, Map<String, Object> datas){
		this.view = view;
		this.datas.putAll(datas);
	}
	
	/**
	 * 返回视图名称，例如 JSP 文件
	 *
	 * @return
	 */
	public String getView(){
		return this.view;
	}
	
	public void setView(String view){
		this.view = view;
	}
	
	public ModelAndView addObject(String key, Object value){
		if(!StringUtils.stringIsEmpty(key))
			datas.put(key, value);
		return this;
	}
	
	public ModelAndView addObject(Map<String, Object> datas){
		if(datas != null && datas.size() > 0)
			this.datas.putAll(datas);
		return this;
	}
	
	public Map<String, Object> getModel(){
		return this.datas;
	}
	
	public Object get(String key){
		return datas.get(key);
	}
	
	public boolean isEmpty(){
		return (this.view == null && (datas == null || datas.size() == 0));
	}
	
	public void clear(){
		this.view = null;
		this.datas.clear();
		this.datas = null;
	}
}