/**
 * 
 */
package com.ge.monitoring.agent.restserver;

import java.util.ArrayList;
import java.util.List;

import com.ge.monitoring.agent.models.MockupData;
import com.ge.monitoring.agent.restserver.RestServer.HttpError;
import com.sun.net.httpserver.HttpExchange;

/**
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */
@SuppressWarnings("restriction")
public class MeasuresRestHandler extends RestHandler {

	/**
	 * Perform GET HTTP request processing. Generate a specific JSON random
	 * valued message.
	 */
	@Override
	public HttpError get(HttpExchange he, RestResponse response) {
		
		List<MockupData> data = new ArrayList<MockupData>();
		data.add(new MockupData(
				(int) (Math.random() * 10000)));
		data.add(new MockupData(
				(int) (Math.random() * 10000)));
		data.add(new MockupData(
				(int) (Math.random() * 10000)));
		data.add(new MockupData(
				(int) (Math.random() * 10000)));
		
		response.addData(data);
		
		return HttpError.OK;
	}
}
