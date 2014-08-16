package com.webcontext.apps.grs.framework.restserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.webcontext.apps.grs.framework.restserver.rest.handler.RestHandler;
import com.webcontext.apps.grs.framework.restserver.rest.handler.WebHandler;

/**
 * Internal HTTP server on a specific port (default is 8888).
 * <p>
 * the rest Server component is a small and dependency reduced HTTP server,
 * serving JSON document. Based only on the HttpServer implementation from Java
 * SE 7, and the GSon library it brings to developer some basic features like
 * Rest request handling with simple Implementation.
 * </p>
 * <p>
 * a basic usage can be
 * </p>
 * 
 * <pre>
 * server = new RestServer(port, stopKey);
 * server.addContext(&quot;/rest/instruments&quot;, new GamesRestHandler(server));
 * server.start();
 * </pre>
 * <p>
 * see {@link README.md} for more information.
 * </p>
 * 
 * @author Fréderic Delorme<frederic.delorme@web-context.com>
 * 
 */

@SuppressWarnings("restriction")
public class RestServer {
	/**
	 * Internal Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(RestServer.class);

	/**
	 * Internal concurrent thread pool queue to server Handler.
	 */
	private static final int POOL_QUEUE_SIZE = 50;
	/**
	 * Number of parallel processing threads.
	 */
	private static final int CORE_POOL_SIZE = 2;
	/**
	 * max number of parallel processing threads where thread pool queue riched
	 * its max.
	 */
	private static final int MAX_CORE_POOL_SIZE = 4;

	/**
	 * HeartBeat frequency for the server to detect if administrative stop is
	 * required.
	 */
	private static final int HEARBEAT_FREQUENCY = 5000;

	/**
	 * boolean flag to activate authentication on administrative and all request
	 * in the RestServer. By default, set to false.
	 */
	private boolean authentication = false;

	/**
	 * HeartBeat flag to manager stop of the server.
	 */
	private long heartBeat = 1;

	/**
	 * This is the internal Server Statistics information object.
	 */
	private ServerInformation info = new ServerInformation(new Date());

	/**
	 * Internal HTTP method This Enum implements all known HTTP methods.
	 * 
	 * @author Frédéric Delorme<frederic.delorme@serphydose.com>
	 * 
	 */
	public enum HttpMethod {
		GET, PUT, POST, DELETE, OPTIONS, HEAD, TRACE, CONNECT
	}

	/**
	 * list of HTTP implemented ERROR code. Only some of those are declared into
	 * this implementation. Please visit http://tools.ietf.org/html/rfc7231 for
	 * details.
	 * 
	 * <table>
	 * <tr>
	 * <th>Code</th>
	 * <th>Message</th>
	 * </tr>
	 * <tbody>
	 * <tr>
	 * <td>200</td>
	 * <td>OK</td>
	 * </tr>
	 * <tr>
	 * <td>201</td>
	 * <td>CREATED</td>
	 * </tr>
	 * <tr>
	 * <td>202</td>
	 * <td>ACCEPTED</td>
	 * </tr>
	 * <tr>
	 * <td>301</td>
	 * <td>MOVE_PERMANENTLY</td>
	 * </tr>
	 * <tr>
	 * <td>302</td>
	 * <td>MOVE_TEMPORARILY</td>
	 * </tr>
	 * <tr>
	 * <td>400</td>
	 * <td>BAD_REQUEST</td>
	 * </tr>
	 * <tr>
	 * <td>401</td>
	 * <td>UNAUTHORIZED</td>
	 * </tr>
	 * <tr>
	 * <td>402</td>
	 * <td>PAYMENT_REQUIRED</td>
	 * </tr>
	 * <tr>
	 * <td>403</td>
	 * <td>FORBIDDEN</td>
	 * </tr>
	 * <tr>
	 * <td>404</td>
	 * <td>NOT_FOUND</td>
	 * </tr>
	 * <tr>
	 * <td>418</td>
	 * <td>I_AM_A_TEAPOT</td>
	 * </tr>
	 * <tr>
	 * <td>500</td>
	 * <td>INTERNAL_ERROR</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see http://tools.ietf.org/html/rfc7231
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
	 * The internal instance of the Sun Http Server.
	 */
	private HttpServer server = null;
	/**
	 * Http port used by the server at startup.
	 */
	private int port = 8888;

	/**
	 * Magic keyword to stop server on the <code>/rest/admin</code> URI.
	 */
	@SuppressWarnings("unused")
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
	 * construction time. In that case, the default stop key is
	 * <code>STOP</code>.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public RestServer(int port) throws IOException {
		this.port = port;
		initServer(port);
	}

	/**
	 * Initialize the server with a specific port,a nd a specific magic Stop
	 * key.
	 * 
	 * @param port
	 * @param stopKey
	 * @throws IOException
	 */
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

		// Create the server
		server = HttpServer.create(new InetSocketAddress(port), 0);

		// initialize its ThreadPool to serve RestHandler
		server.setExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE,
				MAX_CORE_POOL_SIZE, HEARBEAT_FREQUENCY, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(POOL_QUEUE_SIZE)));
		// Add the internal Administrative Handler.
		server.createContext("/rest/admin", new AdminHandler(this));
		addRessourceContext("/web", new WebHandler(this));

		LOGGER.info(String
				.format("Server has just been initialized on port %d, with ThreadPool of [core: %d, max: %d, queue: %d]",
						port, 
						CORE_POOL_SIZE, 
						MAX_CORE_POOL_SIZE,
						POOL_QUEUE_SIZE));
	}

	/**
	 * Start the Rest HTTP server after initialization.
	 * 
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void start() throws InterruptedException, InstantiationException,
			IllegalAccessException {
		if (server != null) {

			boostrapServer();

			server.start();
			LOGGER.info(String.format("Server '%s' on port %d started",
					getServerName(), port));
			while (heartBeat != -1) {
				Thread.sleep(HEARBEAT_FREQUENCY);
				/*
				 * LOGGER.debug(String.format("Rest Server heart beat is alive",
				 * heartBeat));
				 */
				if (heartBeat != -1) {
					heartBeat = 0;
				}

			}
			LOGGER.info("Server has stopped");
		}
	}

	/**
	 * Detect Class inheriting from IBootstrap to perform their processing and
	 * initialize what have to be initialized.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void boostrapServer() throws InstantiationException,
			IllegalAccessException {
		Reflections reflections = new Reflections("com.webcontext.apps");
		Set<Class<?>> classes = reflections
				.getTypesAnnotatedWith(Bootstrap.class);
		for (Class<?> classe : classes) {
			IBootstrap bootStrapped = (IBootstrap) classe.newInstance();
			bootStrapped.initialized();
		}

	}

	/**
	 * Retrieve the Server name from different ways, according to the post from
	 * "Thomas W" on stackoverflow.com .
	 * 
	 * @see http://stackoverflow.com/questions/7348711/20793241#20793241
	 * 
	 * @return
	 */
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

	public void addRestContext(String restPath, RestHandler restHandler) {
		HttpContext hc1 = server.createContext(restPath, restHandler);
		if (authentication) {
			hc1.setAuthenticator(new BasicAuthenticator("rest") {
				@Override
				public boolean checkCredentials(String user, String pwd) {
					return user.equals("admin") && pwd.equals("password");
				}
			});
		}

	}

	public void addRessourceContext(String restPath, WebHandler webHandler) {
		HttpContext hc1 = server.createContext(restPath, webHandler);
		if (authentication) {
			hc1.setAuthenticator(new BasicAuthenticator("web") {
				@Override
				public boolean checkCredentials(String user, String pwd) {
					return user.equals("admin") && pwd.equals("password");
				}
			});
		}

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
	public static int getIntArg(String[] args, String argName, int defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Integer.parseInt(value) : defaultValue);
	}

	/**
	 * retrieve a <code>String</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
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
	public static String getStringArg(String[] args, String argName,
			String defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? value : defaultValue);
	}

	/**
	 * retrieve a <code>Boolean</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
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
	public static Boolean getBooleanArg(String[] args, String argName,
			Boolean defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Boolean.parseBoolean(value) : defaultValue);
	}

	/**
	 * retrieve a <code>Float</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
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
	public static Float getFloatArg(String[] args, String argName,
			Float defaultValue) {
		String value = parseArgs(args, argName);
		return (value != null ? Float.parseFloat(value) : defaultValue);
	}

	/**
	 * Parse all <code>args</code> items and retrieve the <code>argName</code>
	 * value, if exists. Else return null.
	 * 
	 * @param args
	 *            the String[] array containing arguments where to search for
	 *            the <code>argName</code> value.
	 * @param argName
	 *            this is the name of the argument searched for.
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

	/**
	 * @return the info
	 */
	public ServerInformation getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(ServerInformation info) {
		this.info = info;
	}
}
