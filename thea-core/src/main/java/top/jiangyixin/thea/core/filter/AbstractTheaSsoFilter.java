package top.jiangyixin.thea.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.thea.core.common.TheaConstant;
import top.jiangyixin.thea.core.util.AntPathMatcher;
import top.jiangyixin.thea.core.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * AbstractTheaSsoFilter
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午4:20
 */
public abstract class AbstractTheaSsoFilter extends HttpServlet implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AbstractTheaSsoFilter.class);
	
	protected String ssoServer;
	protected String logoutPath;
	protected String excludedPaths;
	protected static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
	protected static final String PATH_SPLIT = ",";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ssoServer = filterConfig.getInitParameter(TheaConstant.SSO_SERVER);
		logoutPath = filterConfig.getInitParameter(TheaConstant.SSO_LOGOUT_PATH);
		excludedPaths = filterConfig.getInitParameter(TheaConstant.SSO_EXCLUDED_PATHS);
        logger.info("{} init success!", this.getClass().getName());
	}
	
	public boolean isExcludePaths(String excludedPaths, String servletPath) {
		if (StringUtils.isNotEmpty(excludedPaths)) {
			for (String excludedPath : excludedPaths.split(PATH_SPLIT)) {
				String urlPattern = excludedPath.trim();
				if (ANT_PATH_MATCHER.match(urlPattern, servletPath)) {
					return true;
				}
			}
		}
		return false;
	}
}
