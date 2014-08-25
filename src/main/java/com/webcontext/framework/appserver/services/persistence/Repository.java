/**
 * 
 */
package com.webcontext.framework.appserver.services.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.webcontext.framework.appserver.model.MDBEntity;

/**
 * @author 212391884
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
	public String dataSource() default "";
	public Class<? extends MDBEntity> entity();
}
