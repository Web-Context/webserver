/**
 * 
 */
package com.webcontext.apps.grs.framework.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * File loader.
 * 
 * @author frederic
 * 
 */
public class FileLoader {
	private static final Logger LOGGER = Logger.getLogger(FileLoader.class);

	/**
	 * Load <code>filePath</code> text file to a String.
	 * 
	 * @param filePath
	 *            File to be read.
	 * @return a string containing auu the file.
	 * @throws IOException
	 */
	public static String loadAsString(String filePath) throws IOException {
		String content = "", line = "";
		LOGGER.debug(String.format("Read file %s into String.", filePath));
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		while ((line = br.readLine()) != null) {
			content += line;
		}
		br.close();
		return content;
	}
}
