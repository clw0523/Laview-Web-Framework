/**
 * @Copyright:Copyright (c) 1991 - 2014
 * @Company: Laview
 */
package com.laview.web.annotation.springmvc;


/**
 * 来自 SpringMVC
 *
 * @author Juergen Hoeller
 * @since: v1.5.2
 */
public interface ValueConstants {

	/**
	 * Constant defining a value for no default - as a replacement for
	 * {@code null} which we cannot use in annotation attributes.
	 * <p>This is an artificial arrangement of 16 unicode characters,
	 * with its sole purpose being to never match user-declared values.
	 * @see RequestParam#defaultValue()
	 * @see RequestHeader#defaultValue()
	 * @see CookieValue#defaultValue()
	 */
	String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";
	
}