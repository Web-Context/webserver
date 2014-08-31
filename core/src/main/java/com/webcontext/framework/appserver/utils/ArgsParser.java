/**
 * 
 */
package com.webcontext.framework.appserver.utils;

/**
 * Java Argument parser to retrieve specific values in the java list of args.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 * 
 */
public class ArgsParser {

	/**
	 * Define list of optional command line server execution arguments with
	 * their Command line argument name and there default value.
	 * 
	 * @author frederic
	 * 
	 */
	public enum ArgType {
		/**
		 * Java server port.
		 */
		SERVER_PORT("port", "8888"),
		/**
		 * Stop key word to use to stop the server.
		 */
		SERVER_STOPKEY("stopkey", "stop"),
		/**
		 * Set of MongoDB database server is embedded or not.
		 */
		DATABASE_EMBEDDED("dbembedded", "false"),
		/**
		 * Set of MongoDB database service port.
		 */
		DATABASE_PORT("dbport", "27017"),
		/**
		 * Flag to activate the verbose mode of the embedded Mongo database.
		 */
		DATABASE_VERBOSE_MODE("dbverbose", "false"),
		/**
		 * Path to data storage for the embedded Mongo database.
		 */
		DATABASE_DATAPATH("dbdatapath", ""),
		/**
		 * Internal concurrent thread pool queue to server Handler.
		 */
		POOL_QUEUE_SIZE("PoolQueueSize", "50"),
		/**
		 * Number of parallel processing threads.
		 */
		CORE_POOL_SIZE("CorePoolSize", "10"),
		/**
		 * max number of parallel processing threads where thread pool queue
		 * riched its max.
		 */
		MAX_CORE_POOL_SIZE("MaxCorePoolSize", "20"),

		/**
		 * HeartBeat frequency for the server to detect if administrative stop
		 * is required.
		 */
		HEARBEAT_FREQUENCY("HeartBeatFrequency", "1000");

		/**
		 * internal attributes to store keyword and default keyword.
		 */
		private String keyword = "", defaultValue = "";

		/**
		 * default constructor to set args and its default keyword.
		 * 
		 * @param keyword
		 * @param defaultValue
		 */
		private ArgType(String code, String defaultValue) {
			this.keyword = code;
			this.defaultValue = defaultValue;
		}

		/**
		 * Retrieve the argument keyword
		 * 
		 * @return
		 */
		public String getKeyword() {
			return keyword;
		}

		/**
		 * retrieve the default value for this argument.
		 * 
		 * @return
		 */
		public String getDefaultValue() {
			return defaultValue;
		}
	}

	private String[] arguments = null;

	/**
	 * Default constructor.
	 */
	public ArgsParser() {
	}

	/**
	 * Initialize list of arguments.
	 * 
	 * @param args
	 */
	public ArgsParser(String[] args) {
		this.arguments = args;
	}

	/**
	 * retrieve an <keyword>int</keyword> parameter name
	 * <keyword>argName</keyword> from the <keyword>args</keyword> list. if
	 * keyword is not set , return the <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is
	 *            not found in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public int getIntArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Integer.parseInt(value) : Integer
				.parseInt(defaultValue));
	}

	/**
	 * retrieve a <keyword>String</keyword> parameter name
	 * <keyword>argName</keyword> from the <keyword>args</keyword> list. if
	 * keyword is not set , return the <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is
	 *            not found in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public String getStringArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? value : defaultValue);
	}

	/**
	 * retrieve a <keyword>Boolean</keyword> parameter name
	 * <keyword>argName</keyword> from the <keyword>args</keyword> list. if
	 * keyword is not set , return the <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is
	 *            not found in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public Boolean getBooleanArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Boolean.parseBoolean(value) : Boolean
				.parseBoolean(defaultValue));
	}

	/**
	 * retrieve a <keyword>Float</keyword> parameter name
	 * <keyword>argName</keyword> from the <keyword>args</keyword> list. if
	 * keyword is not set , return the <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is
	 *            not found in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public Float getFloatArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Float.parseFloat(value) : Float
				.parseFloat(defaultValue));
	}

	/**
	 * Parse all <keyword>args</keyword> items and retrieve the
	 * <keyword>argName</keyword> keyword, if exists. Else return null.
	 * 
	 * @param argName
	 *            this is the name of the argument searched for.
	 * @return
	 */
	private String parseArgs(String argName) {
		String value = null;
		for (String arg : arguments)
			if (arg.startsWith(argName)) {
				String[] argValue = arg.split("=");
				value = argValue[1];
				break;
			}
		return value;
	}

}
