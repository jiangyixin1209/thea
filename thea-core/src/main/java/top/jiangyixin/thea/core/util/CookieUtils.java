package top.jiangyixin.thea.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:33
 */
public class CookieUtils {
	
	private static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
	private static final String COOKIE_PATH = "/";
	
	public static void set(HttpServletResponse response, String name, String value, String domain,
	                       String path, int maxAge, boolean isHttpOnly) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(isHttpOnly);
		response.addCookie(cookie);
	}
	
	public static void set(HttpServletResponse response, String name, String value, boolean isRememberMe) {
		int age = isRememberMe ? COOKIE_MAX_AGE : -1;
		set(response, name, value, null, COOKIE_PATH, age, true);
	}
	
	public static Cookie get(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
			    if (cookie.getName().equals(name)) {
			    	return cookie;
			    }
			}
		}
		return null;
	}
	
	public static String getValue(HttpServletRequest request, String name) {
		Cookie cookie = get(request, name);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}
	
	public static void remove(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookie = get(request, name);
		if (cookie != null) {
			set(response, name, "", null, COOKIE_PATH, 0, true);
		}
	}
}
