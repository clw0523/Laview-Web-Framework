/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.config;

import com.laview.commons.collections.ArrayUtils;
import com.laview.commons.web.RequestMethod;

/**
 * URL + Request 方法的适匹器
 *
 * @author laview_chen
 * @since: v1.0
 */
public class UrlMethodKey {

	private String url;
	
	private RequestMethod[] requestMethods;

	public UrlMethodKey(String url, RequestMethod[] rms){
		this.url = url;
		this.requestMethods = rms;
	}
	
	/**
	 * @param methodPath
	 * @param requestMethod
	 */
	public UrlMethodKey(String url, RequestMethod requestMethod) {
		this(url, new RequestMethod[]{requestMethod});
	}

	/**
	 * 在 RequestMethod 上面包含 mapKey 的 RequestMethod
	 *
	 * @param mapKey
	 * @return
	 */
	public boolean methodsContainer(UrlMethodKey mapKey) {
		return ArrayUtils.isSubArray(requestMethods, mapKey.getRequestMethods());
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the requestMethods
	 */
	public RequestMethod[] getRequestMethods() {
		return requestMethods;
	}

	/**
	 * @param requestMethods the requestMethods to set
	 */
	public void setRequestMethods(RequestMethod[] requestMethods) {
		this.requestMethods = requestMethods;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + Arrays.hashCode(requestMethods);
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/**
	 * 要注意这个方法： 这是一个单向的相等关系，也就是 我被另一个 Key 相等或包含，返回来就不能说是相等。
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlMethodKey other = (UrlMethodKey) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		}
		else if (!url.equals(other.url))
			return false;

		if(requestMethods == null){
			 if(other.requestMethods != null)
				 return false;
		}else if(!ArrayUtils.isSubArray(other.requestMethods,requestMethods)){
			return false;
		}
		
		return true;
	}

}