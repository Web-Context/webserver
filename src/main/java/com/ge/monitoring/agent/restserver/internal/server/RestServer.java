/**
 * 
 */
package com.ge.monitoring.agent.restserver.internal.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ge.monitoring.agent.restserver.internal.rest.RestHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Internal HTTP server on a specific port (default is 8888).
 * 
 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
 * 
 */

@SuppressWarnings("restriction")
public class RestServer {
	private static final Logger LOGGER = Logger.getLogger(RestServer.class);
	private long heartBeat = 1;

	/**
	 * Internal HTTP method
	 * 
	 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
	 * 
	 */
	public enum HttpMethod {
		GET, PUT, POST, DELETE, OPTIONS, HEAD, TRACE, CONNECT
	}

	/**
	 * list of HTTP implemented ERROR code.
	 * 
	 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
	 * 
	 */
	public enum HttpStatus {
		OK(200), CREATED(201), ACCEPTED(202), MOVE_PERMANENTLY(301), MOVED_TEMPORARILY(
				302), BAD_REQUEST(400), UNAUTHORIZED(401), PAYMENT_REQUIRED(402), FORBIDDEN(
				403), NOT_FOUNT(404), I_AM_A_TEAPOT(418), INTERNAL_ERROR(500);

		private int code;

		private HttpStatus(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	/**
	 * Sun SE Http Server
	 */
	private HttpServer server = null;
	/**
	 * Http port used by the server at startup.
	 */
	private int port = 8888;

	private String stopkey = "";

	/**
	 * Default constructor initializing HTTP Rest server on the default port (8
	 * 888).
	 * 
	 * @throws IOException
	 */
	public RestServer() throws IOException {
		initServer(this.port);
	}

	/**
	 * Initialize the Rest HTTP Server on the specified <code>port</code> à
	 * construction time.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public RestServer(int port) throws IOException {
		this.port = port;
		initServer(port);
	}

	public RestServer(int port, String stopKey) throws IOException {
		this(port);
		this.stopkey = stopKey;
	}

	/**
	 * Initialize the Rest HTTP Server.
	 * 
	 * @param port
	 * @throws IOException
	 */
	private void initServer(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.setExecutor(new ThreadPoolExecutor(2, 4, 1000,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue(50)));
		server.createContext("/rest/admin", new AdminHandler(this));
		LOGGER.info("Server has just been initialized on port " + port);
	}

	/**
	 * Start the Rest HTTP server after initialization.
	 * 
	 * @throws InterruptedException
	 */
	public void start() throws InterruptedException {
		if (server != null) {
			server.start();
			LOGGER.info(String.format("Server '%s' on port %d started",
					getServerName(), port));
			while (heartBeat != -1) {
				Thread.sleep(1000);
				LOGGER.debug(String.format("Rest Server heart beat is alive",
						heartBeat));
				if (heartBeat != -1) {
					heartBeat = 0;
				}

			}
			LOGGER.info("Server has stopped");
		}
	}

	private String getServerName() {
		// try InetAddress.LocalHost first;
		// NOTE -- InetAddress.getLocalHost().getHostName() will not work in
		// certain environments.
		try {
			String result = InetAddress.getLocalHost().getHostName();
			if (result != null && !result.equals(""))
				return result;
		} catch (UnknownHostException e) {
			// failed; try alternate means.
		}

		// try environment properties.
		//
		String host = System.getenv("COMPUTERNAME");
		if (host != null)
			return host;
		host = System.getenv("HOSTNAME");
		if (host != null)
			return host;

		// undetermined.
		return null;
	}

	/**
	 * Stop the Rest HTTP Server.
	 */
	public void stop() {
		if (server != null) {
			server.stop(0);
			heartBeat = -1;
			LOGGER.info("Ask server to Stop");
		}
	}

	public void addContext(String restPath, RestHandler restHandler) {
		server.createContext(restPath, restHandler);
	}

	/**
	 * retrieve an <code>int</code> parameter name <code>argName</code> from the
	 * <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param args
	 *            list of arguments from the Java main method.
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	@SuppressWarnings("unused")
	public static int getIntArg(String[] args, String argName, int defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Integer.parseInt(value) : defaultValue);
	}

	@SuppressWarnings("unused")
	public static String getStringArg(String[] args, String argName,
			String defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? value : defaultValue);
	}

	@SuppressWarnings("unused")
	public static Boolean getBooleanArg(String[] args, String argName,
			Boolean defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Boolean.parseBoolean(value) : defaultValue);
	}

	@SuppressWarnings("unused")
	public static Float getFloatArg(String[] args, String argName,
			Float defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Float.parseFloat(value) : defaultValue);
	}

	/**
	 * @param args
	 * @param argName
	 * @return
	 */
	private static String parseArgs(String[] args, String argName) {
		String value = null;
		for (String arg : args)
			if (arg.startsWith(argName)) {
				String[] argValue = arg.split("=");
				value = argValue[1];
				break;
			}
		return value;
	}

}
