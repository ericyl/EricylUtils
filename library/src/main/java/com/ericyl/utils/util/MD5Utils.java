package com.ericyl.utils.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Utils {

	public static String getStringMD5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			char[] charArray = str.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			byte[] digestData = md5.digest(byteArray);
			return byteHex(digestData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getFileMD5(File file) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			FileInputStream in = new FileInputStream(file);
			// FileChannel ch = in.getChannel();
			// MappedByteBuffer byteBuffer =
			// ch.map(FileChannel.MapMode.READ_ONLY,
			// 0, file.length());
			// alg.update(byteBuffer);
			byte[] buffer = new byte[2048];
			int length;
			while ((length = in.read(buffer)) != -1) {
				md5.update(buffer, 0, length);
			}
			byte[] digestData = md5.digest();
			return byte2hex(digestData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private static String byteHex(byte[] bytes) {
		StringBuilder hs = new StringBuilder();
		String tmp;
		for (byte b : bytes) {
			tmp = (Integer.toHexString(b & 0XFF));
			if (tmp.length() == 1)
				hs.append("0").append(tmp);
			else
				hs.append(tmp);
		}
		return hs.toString().toUpperCase();
	}

	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String tmp;
		for (int n = 0; n < b.length; n++) {
			tmp = (Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1)
				hs.append("0").append(tmp);
			else
				hs.append(tmp);
			if (n < b.length - 1)
				hs.append(":");
		}
		return hs.toString().toUpperCase();
	}

}
