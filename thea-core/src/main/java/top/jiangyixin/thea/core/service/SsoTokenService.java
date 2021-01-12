package top.jiangyixin.thea.core.service;

import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.exception.TheaException;
import top.jiangyixin.thea.core.helper.SsoLoginStoreHelper;
import top.jiangyixin.thea.core.helper.SsoUserHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * SsoTokenService
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:27
 */
public class SsoTokenService {
	
	public static void login(String sessionId, SsoUser ssoUser) {
		String userId = SsoUserHelper.getUserId(sessionId);
		if (userId == null) {
			throw new TheaException("getUserId from sessionId Fail, SessionId: " + sessionId);
		}
		SsoLoginStoreHelper.put(userId, ssoUser);
	}
	
	public static void logout(String sessionId) {
		String userId = SsoUserHelper.getUserId(sessionId);
		if (userId == null) {
			return;
		}
		SsoLoginStoreHelper.remove(userId);
	}
	
	public static void logout(HttpServletRequest request) {
		String sessionId = request.getHeader(SsoConfig.SSO_SESSION_ID);
		logout(sessionId);
	}
	
	public static SsoUser loadUser(String sessionId) {
		return SsoUserHelper.loadSsoUser(sessionId);
	}
	
	public static SsoUser loadUser(HttpServletRequest request) {
		String sessionId = request.getHeader(SsoConfig.SSO_SESSION_ID);
		return loadUser(sessionId);
	}
}
