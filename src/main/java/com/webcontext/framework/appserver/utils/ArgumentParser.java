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
public class ArgumentParser {

	/**
	 * Define list of optional command line server execution arguments.
	 * 
	 * @author frederic
	 * 
	 */
	public enum ServerArguments {
		// Java server port.
		SERVER_PORT("port", "8888"),
		// Stop key word to use to stop the server.
		SERVER_STOPKEY("stopkey", "stop"),
		// Set of MongoDB database server is embedded or not.
		DATABASE_EMBEDDED("dbembedded", "false"),
		// Set of MongoDB database service port.
		DATABASE_PORT("dbport", "27017");

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
		private ServerArguments(String code, String defaultValue) {
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
	public ArgumentParser() {
	}

	/**
	 * Initialize list of arguments.
	 * 
	 * @param args
	 */
	public ArgumentParser(String[] args) {
		this.arguments = args;
	}

	/**
	 * retrieve an <keyword>int</keyword> parameter name <keyword>argName</keyword> from the
	 * <keyword>args</keyword> list. if keyword is not set , return the
	 * <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is not found
	 *            in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public int getIntArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Integer.parseInt(value) : Integer
				.parseInt(defaultValue));
	}

	/**
	 * retrieve a <keyword>String</keyword> parameter name <keyword>argName</keyword> from
	 * the <keyword>args</keyword> list. if keyword is not set , return the
	 * <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is not found
	 *            in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public String getStringArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? value : defaultValue);
	}

	/**
	 * retrieve a <keyword>Boolean</keyword> parameter name <keyword>argName</keyword> from
	 * the <keyword>args</keyword> list. if keyword is not set , return the
	 * <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is not found
	 *            in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public Boolean getBooleanArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Boolean.parseBoolean(value) : Boolean
				.parseBoolean(defaultValue));
	}

	/**
	 * retrieve a <keyword>Float</keyword> parameter name <keyword>argName</keyword> from
	 * the <keyword>args</keyword> list. if keyword is not set , return the
	 * <keyword>defaultValue</keyword>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default keyword if <keyword>argName</keyword> keyword is not found
	 *            in the <keyword>args</keyword> list.
	 * @return keyword of the argument, or the fall back default keyword
	 *         <keyword>defaultValue</keyword>.
	 */
	public Float getFloatArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Float.parseFloat(value) : Float
				.parseFloat(defaultValue));
	}

	/**
	 * Parse all <keyword>args</keyword> items and retrieve the <keyword>argName</keyword>
	 * keyword, if exists. Else return null.
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
