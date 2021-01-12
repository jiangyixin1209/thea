package top.jiangyixin.thea.core.service;

import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.exception.TheaException;
import top.jiangyixin.thea.core.helper.SsoLoginStoreHelper;
import top.jiangyixin.thea.core.helper.SsoUserHelper;
import top.jiangyixin.thea.core.util.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SsoWebService
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:27
 */
public class SsoWebService {
	
	/**
	 * 客户端登录
	 * @param response          response
	 * @param sessionId         SessionId(User.id_User.version)
	 * @param ssoUser           ssoUser
	 * @param isRememberMe      是否记住
	 */
	public static void login(HttpServletResponse response, String sessionId,
	                         SsoUser ssoUser, boolean isRememberMe) {
		String userId = SsoUserHelper.getUserId(sessionId);
		if (userId == null) {
			throw new TheaException("parseSessionId Fail, session: " + sessionId);
		}
		SsoLoginStoreHelper.put(userId, ssoUser);
		CookieUtils.set(response, SsoConfig.SSO_SESSION_ID, sessionId, isRememberMe);
	}
	
	/**
	 * 客户端登出
	 * @param request       request
	 * @param response      response
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = CookieUtils.getValue(request, SsoConfig.SSO_SESSION_ID);
		if (sessionId == null) {
			return;
		}
		String userId = SsoUserHelper.getUserId(sessionId);
		if (userId != null) {
			SsoLoginStoreHelper.remove(userId);
		}
		CookieUtils.remove(request, response, SsoConfig.SSO_SESSION_ID);
	}
	
	public static SsoUser loadUser(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = CookieUtils.getValue(request, SsoConfig.SSO_SESSION_ID);
		SsoUser ssoUser = SsoUserHelper.loadSsoUser(sessionId);
		if (ssoUser != null) {
			return ssoUser;
		}
		removeSessionIdByCookie(request, response);
		String paramSessionId = request.getParameter(SsoConfig.SSO_SESSION_ID);
		ssoUser = SsoUserHelper.loadSsoUser(paramSessionId);
		if (ssoUser != null) {
			CookieUtils.set(response, SsoConfig.SSO_SESSION_ID, paramSessionId, false);
			return ssoUser;
		}
		return null;
	}
	
	/**
	 * 将Session id 从Cookie中删除
	 * @param request   request
	 * @param response  response
	 */
	public static void removeSessionIdByCookie(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.remove(request, response, SsoConfig.SSO_SESSION_ID);
	}
	
	public static String getSessionIdByCookie(HttpServletRequest request) {
		return CookieUtils.getValue(request, SsoConfig.SSO_SESSION_ID);
	}
	
}
