/**
 * 
 */
package com.ge.monitoring.agent.restserver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ge.monitoring.agent.models.MockupData;
import com.ge.monitoring.agent.restserver.internal.HttpRequest;
import com.ge.monitoring.agent.restserver.internal.RestHandler;
import com.ge.monitoring.agent.restserver.internal.RestResponse;
import com.ge.monitoring.agent.restserver.internal.RestServer.HttpError;

/**
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */
public class MeasuresRestHandler extends RestHandler {
	private static final Logger LOGGER = Logger.getLogger(MeasuresRestHandler.class);

	/**
	 * Perform GET HTTP request processing. Generate a specific JSON random
	 * valued message.
	 */
	@Override
	public HttpError get(HttpRequest request, RestResponse response) {

		String title = (String) request.getParameter("title", "no-title")
				.toArray()[0];
		String version = (String) request.getParameter("version", "1.0")
				.toArray()[0];
		int nb = Integer
				.parseInt((String) request.getParameter("nb").toArray()[0]);

		LOGGER.debug(String.format("Parameters title=%s, version=%s, nb=%d", title,version,nb));
		
		if (nb != 0 && title != null && version != null) {
			List<MockupData> measures = new ArrayList<MockupData>();
			for (int i = 0; i < nb; i++) {
				measures.add(new MockupData(title, version,
						(int) (Math.random() * 10000)));
			}
			response.add("measures",measures);
			LOGGER.debug(String.format("created nb=%d objects", nb));
		}

		return HttpError.OK;
	}
}
