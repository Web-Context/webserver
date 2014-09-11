/**
 * 
 */
package com.webcontext.framework.appserver.services.web.response.handler.impl.web;

import java.io.OutputStream;

import com.webcontext.framework.appserver.services.web.response.io.HttpResponse;

/**
 * A WebResponse object return by the WebHandler implementation.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class WebResponse extends HttpResponse<String> {

	public WebResponse(OutputStream outputstream) {
		super(outputstream);
		this.data = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.webcontext.framework.appserver.services.web.http.HttpResponse#process()
	 */
	@Override
	public String process() {
		// simple return data from the response object.
		return this.data;
	}

	@Override
	public void add(String data) {
		this.data += data;

	}
}
