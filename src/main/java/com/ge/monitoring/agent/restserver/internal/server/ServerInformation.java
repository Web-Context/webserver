/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal.server;

import java.util.Date;

/**
 * Internal Server information.
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 *
 */
public class ServerInformation {

	private Date startDate;
	private Date lastRequest;
	private long requestCounter;
	private long successfulRequestCounter;
	private long errorRequestCounter;

	/**
	 * Default constructor initializing the start Date.
	 * 
	 * @param startDate
	 */
	public ServerInformation(Date startDate) {
		this.setStartDate(startDate);
	}

	/**
	 * @return the successfulRequestCounter
	 */
	public long getSuccessfulRequestCounter() {
		return successfulRequestCounter;
	}

	/**
	 * @param successfulRequestCounter
	 *            the successfulRequestCounter to set
	 */
	public void setSuccessfulRequestCounter(long successfulRequestCounter) {
		this.successfulRequestCounter = successfulRequestCounter;
	}

	/**
	 * @return the errorRequestCounter
	 */
	public long getErrorRequestCounter() {
		return errorRequestCounter;
	}

	/**
	 * @param errorRequestCounter
	 *            the errorRequestCounter to set
	 */
	public void setErrorRequestCounter(long errorRequestCounter) {
		this.errorRequestCounter = errorRequestCounter;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the lastRequest
	 */
	public Date getLastRequest() {
		return lastRequest;
	}

	/**
	 * @param lastRequest
	 *            the lastRequest to set
	 */
	public void setLastRequest(Date lastRequest) {
		this.lastRequest = lastRequest;
	}

	/**
	 * @return the requestCounter
	 */
	public long getRequestCounter() {
		return requestCounter;
	}

	/**
	 * @param requestCounter
	 *            the requestCounter to set
	 */
	public void setRequestCounter(long requestCounter) {
		this.requestCounter = requestCounter;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
