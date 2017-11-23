package com.li.test.util;


public class HttpUrl {
	// TOKEN
	public static final String TOKEN = "1111";

	public static String appendUrl(String url) {
		try {
			url = url + "?token=" + TOKEN;
		} catch (Exception e) {
			LogUtil.e(HttpUrl.class, "appendUrl", e);
		}
		return url;
	}
	public static final String URL = "http://ttttt";
	/** 登录 */
	public static final String HTTP_LOGIN = URL + "/api/Account/Login";
}
