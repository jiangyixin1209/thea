package top.jiangyixin.thea.core.helper;

import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.util.StringUtils;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午3:24
 */
public class SsoUserHelper {
	
	public static final String SPLIT_SYMBOL = "_";
	public static final String STORE_ID = "STORE_ID";
	public static final String STORE_VERSION = "STORE_VERSION";
	
	public static SsoUser loadSsoUser(String sessionId) {
		String userId = parseSessionId(sessionId, STORE_ID);
		String version = parseSessionId(sessionId, STORE_VERSION);
		SsoUser ssoUser = SsoLoginStoreHelper.get(userId);
		if (ssoUser != null) {
			if (ssoUser.getVersion().equals(version)) {
				if (System.currentTimeMillis() - ssoUser.getLastFreshTime() > ssoUser.getExpireMinute()/2) {
					ssoUser.setLastFreshTime(System.currentTimeMillis());
					SsoLoginStoreHelper.put(userId, ssoUser);
				}
				return ssoUser;
			}
		}
		return null;
	}
	
	public static String parseSessionId(String sessionId, String type) {
		if (sessionId != null && sessionId.contains(SPLIT_SYMBOL)) {
			String[] result = sessionId.split(SPLIT_SYMBOL);
			if (STORE_ID.equals(type)) {
				if (StringUtils.isNotEmpty(result[0])) {
					return result[0].trim();
				}
			} else if (STORE_VERSION.equals(type)) {
				if (StringUtils.isNotEmpty(result[1])) {
					return result[1].trim();
				}
			}
		}
		return null;
	}
	
	public static String getUserId(String sessionId) {
		return parseSessionId(sessionId, STORE_ID);
	}
	
	public static String getVersion(String sessionId) {
		return parseSessionId(sessionId, STORE_VERSION);
	}
	
	public static String makeSessionId(SsoUser user) {
		return user.getUserId().concat("_").concat(user.getVersion());
	}
}
