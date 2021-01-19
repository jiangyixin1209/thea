package top.jiangyixin.thea.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.service.SsoWebService;
import top.jiangyixin.thea.core.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SsoWebFilter
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午2:53
 */
public class SsoWebFilter extends AbstractSsoFilter {
	private static final Logger logger = LoggerFactory.getLogger(SsoWebFilter.class);
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
	                     FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String servletPath = request.getServletPath();
		
		// 检查是否为排除验证路径的urlPath
		if (StringUtils.isNotEmpty(excludedPaths) && isExcludePaths(excludedPaths, servletPath)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// 检查是否当前为登出路径操作
		if (StringUtils.isNotEmpty(logoutPath) && logoutPath.equals(servletPath)) {
			SsoWebService.removeSessionIdByCookie(request, response);
			
			// 跳转到SSO中心的logout地址
			String logoutPath = ssoServer.concat(SsoConfig.SSO_LOGOUT);
			response.sendRedirect(logoutPath);
			return;
		}
		
		SsoUser ssoUser = SsoWebService.loadUser(request, response);
		
		// 非法用户
		if (ssoUser == null) {
			String contentType = request.getHeader("content-type");
			boolean json = contentType != null && contentType.contains("json");
			if (json) {
				response.setContentType("application/json;charset=utf-8");
                response.getWriter().println("{\"code\":500,\"message\":\"SSO未登录\"}");
                return;
			}
			String link = request.getRequestURL().toString();
			// 跳转到sso中心进行登录
			String loginPath = ssoServer.concat(SsoConfig.SSO_LOGIN)
					.concat("?").concat(SsoConfig.REDIRECT_URL).concat("=").concat(link);
			response.sendRedirect(loginPath);
			return;
		}
		
		// 已登录则，设置ssoUser到request
		request.setAttribute(SsoConfig.SSO_USER, ssoUser);
		filterChain.doFilter(request, response);
	}
}
