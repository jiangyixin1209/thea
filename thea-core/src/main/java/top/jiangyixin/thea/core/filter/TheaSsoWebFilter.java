package top.jiangyixin.thea.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.thea.core.common.TheaConstant;
import top.jiangyixin.thea.core.util.AntPathMatcher;
import top.jiangyixin.thea.core.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午2:53
 */
public class TheaSsoWebFilter extends HttpServlet implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(TheaSsoWebFilter.class);
	
	private String ssoServer;
	private String logoutPath;
	private String excludedPaths;
	private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ssoServer = filterConfig.getInitParameter(TheaConstant.SSO_SERVER);
		logoutPath = filterConfig.getInitParameter(TheaConstant.SSO_LOGOUT_PATH);
		excludedPaths = filterConfig.getInitParameter(TheaConstant.SSO_EXCLUDED_PATHS);
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
	                     FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String requestPath = request.getServletPath();
		if (StringUtils.isNotEmpty(excludedPaths)) {
			for (String excludedPath : excludedPaths.split(",")) {
			    String urlPattern = excludedPath.trim();
			    if (antPathMatcher.match(urlPattern, requestPath)) {
			    	filterChain.doFilter(request, response);
			    	return;
			    }
			}
		}
		
		if (StringUtils.isNotEmpty(logoutPath) && logoutPath.equals(requestPath)) {
		
		}
	
	}
}
