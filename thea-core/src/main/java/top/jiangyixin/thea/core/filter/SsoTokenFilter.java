package top.jiangyixin.thea.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.service.SsoTokenService;
import top.jiangyixin.thea.core.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午4:12
 */
public class SsoTokenFilter extends AbstractSsoFilter {
	private static final Logger logger = LoggerFactory.getLogger(SsoTokenFilter.class);
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
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
			SsoTokenService.logout(request);
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().println("{\"code\":200,\"message\":\"操作成功\"}");
			return;
		}
		
		SsoUser ssoUser = SsoTokenService.loadUser(request);
		if (ssoUser == null) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().println("{\"code\":500,\"message\":\"SSO未登录\"}");
			return;
		}
		request.setAttribute(SsoConfig.SSO_USER, ssoUser);
		filterChain.doFilter(request, response);
	}
}
