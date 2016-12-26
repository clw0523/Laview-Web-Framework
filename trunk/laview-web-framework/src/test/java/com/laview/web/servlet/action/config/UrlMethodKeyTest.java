/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Macroview
 */
package com.laview.web.servlet.action.config;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.laview.commons.web.RequestMethod;

/**
 * 测试 UrlMethodAdapter 的相等判断
 *
 * @author Jai
 * @since: v1.0
 */
public class UrlMethodKeyTest {

	@Test
	public void equalsTest(){
		UrlMethodKey url = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET});
		UrlMethodKey url1 = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET});
		
		Assert.assertTrue(url.equals(url1));
		
		UrlMethodKey url2 = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET, RequestMethod.POST});
		UrlMethodKey url3 = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET});
		
		Assert.assertTrue(url3.equals(url2));
		
		UrlMethodKey url4 = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET, RequestMethod.POST});
		UrlMethodKey url5 = new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.PUT});
		
		Assert.assertFalse(url5.equals(url4));
		
		Set<UrlMethodKey> set = new HashSet<UrlMethodKey>();
		set.add(new UrlMethodKey("/path", new RequestMethod[]{RequestMethod.GET, RequestMethod.POST}));
		set.add(new UrlMethodKey("/path2", new RequestMethod[]{RequestMethod.GET}));
		
		Assert.assertTrue(set.contains(url1));
		Assert.assertTrue(set.contains(url2));
		Assert.assertFalse(set.contains(url5));
		Assert.assertFalse(set.contains(new UrlMethodKey("/path2", new RequestMethod[]{RequestMethod.POST})));
	}
}
