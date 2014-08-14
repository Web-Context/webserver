/**
 * 
 */
package com.webcontext.apps.grs.framework.restserver.rest.handler;

import com.webcontext.apps.grs.framework.restserver.http.HttpResponse;

/**
 * @author 212391884
 *
 */
public class WebResponse extends HttpResponse<String> {

	
	public WebResponse() {
		super();
		this.data = "";
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.webcontext.apps.grs.framework.restserver.http.HttpResponse#process()
	 */
	@Override
	public String process() {

		return this.data;
	}

	@Override
	public void add(String data) {
		this.data += data;

	}

}
