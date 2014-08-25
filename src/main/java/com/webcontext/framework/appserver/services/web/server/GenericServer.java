package com.webcontext.framework.appserver.services.web.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import com.webcontext.framework.appserver.services.persistence.DataManager;
import com.webcontext.framework.appserver.services.persistence.Repository;
import com.webcontext.framework.appserver.services.web.response.handler.ContextHandler;
import com.webcontext.framework.appserver.services.web.response.handler.IResponseHandler;
import com.webcontext.framework.appserver.services.web.server.admin.ServerInformation;
import com.webcontext.framework.appserver.services.web.server.bootstrap.Bootstrap;
import com.webcontext.framework.appserver.services.web.server.bootstrap.IBootstrap;
import com.webcontext.framework.appserver.utils.ArgumentParser;

/**
 * Internal HTTP server on a specific port (default is 8888).
 * <p>
 * the rest Server component is a small and dependency reduced HTTP server,
 * serving JSON document. Based only on the HttpServer implementation from Java
 * SE 7, and the GSon library it brings to developer some basic features like
 * Rest request handling with simple Implementation.
 * </p>
 * 
 * <p>
 * This light Server implementation will detect :
 * <ul>
 * <li><code>@Bootstrap</code> annotated components/li>
 * <li>and <code>@Repository</code> annotated Components, to respectively,
 * Initialize things, serve and persists data to some data container,</li>
 * <li>and will initialize specific <code>@ContextHandler</code> annotated
 * components to serve REST and web data.</li>
 * </ul>
 * </p>
 * <p>
 * a basic usage can be
 * </p>
 * 
 * <pre>
 * server = new GenericServer(port, stopKey);
 * server.addContext(&quot;/rest/instruments&quot;, new GamesRestHandler(server));
 * server.start();
 * </pre>
 * <p>
 * see {@link README.md} for more information.
 * 
 * </p>
 * 
 * @author Fréderic Delorme<frederic.delorme@web-context.com>
 * @see Bootstrap
 * @see ContextHandler
 * @see Repository
 */

@SuppressWarnings("restriction")
public class GenericServer {
	/**
	 * Internal Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(GenericServer.class);

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
	 * in the GenericServer. By default, set to false.
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
				403), NOT_FOUND(404), I_AM_A_TEAPOT(418), INTERNAL_ERROR(500);

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

	private ArgumentParser argsParser = null;

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
	public GenericServer() throws IOException {
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
	public GenericServer(int port) throws IOException {
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
	public GenericServer(int port, String stopKey) throws IOException {
		this(port);
		this.stopkey = stopKey;
	}

	/**
	 * Start the server with arguments from command line, analyzed by the
	 * <code>ArgurmentParser</code>.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public GenericServer(String[] args) throws IOException {
		argsParser = new ArgumentParser(args);
		this.port = argsParser.getIntArg(
				ArgumentParser.ServerArguments.SERVER_PORT.getKeyword(),
				ArgumentParser.ServerArguments.SERVER_PORT.getDefaultValue());
		this.stopkey = argsParser
				.getStringArg(ArgumentParser.ServerArguments.SERVER_STOPKEY
						.getKeyword(),
						ArgumentParser.ServerArguments.SERVER_STOPKEY
								.getDefaultValue());
		initServer(this.port);
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

		// initialize repositories.
		DataManager.getInstance().autoRegister();

		try {
			registerHandlers(this);
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error("Unable to initialize ContextHandler", e);
		}

		LOGGER.info(String
				.format("Server has just been initialized on port %d, with ThreadPool of [core: %d, max: %d, queue: %d]",
						port, CORE_POOL_SIZE, MAX_CORE_POOL_SIZE,
						POOL_QUEUE_SIZE));
	}

	/**
	 * Start the Rest HTTP server after initialization.
	 * 
	 * @throws InterruptedException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public void start() throws InterruptedException, InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
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
	 * Detect Class inheriting from IBootstrap to perform their processing and
	 * initialize what have to be initialized.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void registerHandlers(GenericServer server)
			throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException {
		Reflections reflections = new Reflections("com.webcontext");
		Set<Class<?>> classes = reflections
				.getTypesAnnotatedWith(ContextHandler.class);
		for (Class<?> classe : classes) {
			String path = classe.getAnnotation(ContextHandler.class).path();
			// Retrieve the default constructor for GenericSever.
			Constructor<?> ctr = classe
					.getDeclaredConstructor(GenericServer.class);
			// build this instance.
			IResponseHandler handler = (IResponseHandler) ctr
					.newInstance(server);
			addContext(path, handler);

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

	/**
	 * Add a handler to the context of the server. This will add a new
	 * <code>IResponseHandler</code> to the context server.
	 * 
	 * @param restPath
	 *            the URL path to serve with this handler.
	 * @param handler
	 *            the IResponseHandler to connect to the <code>path</code>.
	 */
	public void addContext(String restPath, IResponseHandler handler) {
		HttpContext hc1 = server.createContext(restPath, handler);
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
