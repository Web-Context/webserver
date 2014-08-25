/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.handler;

/**
 * @author 212391884
 *
 */
public @interface ContextHandler {
	public String path() default "/";
}
