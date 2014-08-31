/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 212391884
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextHandler {
	public String path() default "/";
}
