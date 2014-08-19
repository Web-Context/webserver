package com.webcontext.framework.appserver.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
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
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		while ((line = br.readLine()) != null) {
			content += line;
		}
		br.close();
		return content;
	}

	/**
	 * Quick reading of any file.
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String fastRead(String name) throws IOException {
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(
				name));
		byte[] barray = new byte[256];
		String content = "";
		while ((f.read(barray, 0, 256)) != -1)
			content += new String(barray);
		f.close();
		return content;
	}
}
