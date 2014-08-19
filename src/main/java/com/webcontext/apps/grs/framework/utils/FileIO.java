package com.webcontext.apps.grs.framework.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * File IO utils.
 * 
 * @author frederic
 * 
 */
public class FileIO {

	/**
	 * try to detect the Type of a file.
	 * 
	 * @param path
	 * @return
	 */
	public static String getContentType(String path) {
		String type = "";
		try {
			type = Files.probeContentType(FileSystems.getDefault().getPath(
					getResource(path)));
		} catch (IOException e) {
			// Should never happen.
			e.printStackTrace();
			type = "text/plain";
		}
		return type;
	}

	/**
	 * Gets an absolute path from a relative path
	 * 
	 * @param path
	 *            - The relative path of a resource
	 * @return The relative path's absolute path
	 */
	public static String getResource(String path) {
		try {
			return URLDecoder.decode(ClassLoader.getSystemClassLoader()
					.getResource(URLDecoder.decode(path, "UTF-8")).getPath(),
					"UTF-8");
		} catch (UnsupportedEncodingException | NullPointerException e) {
			// This will only happen if the file doesn't exist and is handled
			// later.
			return "";
		}
	}
}
