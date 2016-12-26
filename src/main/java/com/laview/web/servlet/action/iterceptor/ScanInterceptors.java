/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.servlet.action.iterceptor;

import org.apache.log4j.Logger;

import com.laview.commons.lang.Callback;
import com.laview.commons.lang.ReflectUtils;
import com.laview.container.config.bean.ScanBean;
import com.laview.container.core.BeanInfo;

/**
 * 扫描那些 拦截器 （即实现  Interceptor 接口的类）
 *
 * @author laview_chen
 * @since: v1.0
 */
public class ScanInterceptors implements ScanBean{

	private final static Logger logger = Logger.getLogger(ScanInterceptors.class);
	
	/* (non-Javadoc)
	 * @see com.laview.container.config.bean.ScanBean#doWith(com.laview.container.core.BeanInfo)
	 */
	@Override
	public void doWith(BeanInfo bean) {
		if(bean.canInstanceAndImplements(Interceptor.class)){
			logger.debug("[LWF]==> 扫描到 Interceptor，类名[" + bean.getBeanName() + "]");
			
			try {
				RequestInterceptorFactory.addInterceptor((Interceptor)bean.getInstance());
			}
			catch (Exception e) {
				logger.error("[LWF]==> 创建[Interceptor：" + bean.getBeanName() + "]失败，异常信息：", e);
			}
		}
	}
}