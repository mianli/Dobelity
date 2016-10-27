package com.study.mli.dobe.utils.loader.cache;

import java.security.MessageDigest;

/**
 * Created by crown on 2016/10/26.
 */
public class MD5Encoder {

	public static String encode(String text) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("md5");
			byte[] bytes = messageDigest.digest(text.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : bytes) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
