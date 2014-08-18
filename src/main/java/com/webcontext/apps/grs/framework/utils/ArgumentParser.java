/**
 * 
 */
package com.webcontext.apps.grs.framework.utils;

/**
 * Java Argument parser to retrieve specific values in the java list of args.
 * 
 * @author Frédéric Delorme<frederic.delorme@web-context.com>
 *
 */
public class ArgumentParser {

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
	 * retrieve an <code>int</code> parameter name <code>argName</code> from the
	 * <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	public int getIntArg(String argName, int defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Integer.parseInt(value) : defaultValue);
	}

	/**
	 * retrieve a <code>String</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	public String getStringArg(String argName, String defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? value : defaultValue);
	}

	/**
	 * retrieve a <code>Boolean</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	public Boolean getBooleanArg(String argName, Boolean defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Boolean.parseBoolean(value) : defaultValue);
	}

	/**
	 * retrieve a <code>Float</code> parameter name <code>argName</code> from
	 * the <code>args</code> list. if value is not set , return the
	 * <code>defaultValue</code>.
	 * 
	 * @param argName
	 *            argument name to search in the list
	 * @param defaultValue
	 *            the default value if <code>argName</code> value is not found
	 *            in the <code>args</code> list.
	 * @return value of the argument, or the fall back default value
	 *         <code>defaultValue</code>.
	 */
	public Float getFloatArg(String argName, Float defaultValue) {
		String value = parseArgs(argName);
		return (value != null ? Float.parseFloat(value) : defaultValue);
	}

	/**
	 * Parse all <code>args</code> items and retrieve the <code>argName</code>
	 * value, if exists. Else return null.
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
