package top.jiangyixin.thea.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:17
 */
public class StringUtils {
	
	/**
	 *
	 * @param str
	 * @return
	 */
	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}
	
	/**
	 *
	 * @param str
	 * @return
	 */
	public static boolean hasText(String str) {
		return (hasLength(str) && containsText(str));
	}
	
	private static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 *
	 * @param str
	 * @param delimiters
	 * @param trimTokens
	 * @param ignoreEmptyTokens
	 * @return
	 */
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
		
		if (str == null) {
			return null;
		}
		
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}
	
	/**
	 *
	 * @param collection
	 * @return
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}
	
	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}
}
