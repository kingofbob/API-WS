package com.soongwei.meow.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class HttpHelper {
	public static String convertStreamToString(InputStream is, String encoding) throws UnsupportedEncodingException {
		InputStreamReader isr;
		if (encoding != null) {
			isr = new InputStreamReader(is, encoding);
		} else {
			isr = new InputStreamReader(is);
		}
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String convertStreamToString(InputStream is) {
		try {
			return convertStreamToString(is, null);
		} catch (UnsupportedEncodingException e) {
			// Since this is going to use the default encoding, it is never
			// going to crash on an UnsupportedEncodingException
			e.printStackTrace();
			return null;
		}
	}

	public static String encodeURIComponent(String s) {
		String result = "";

		result = s.replaceAll("\\s+", "%20");

		return result;
	}

}
