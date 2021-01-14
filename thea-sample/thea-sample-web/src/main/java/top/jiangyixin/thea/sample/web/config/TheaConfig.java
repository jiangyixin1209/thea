package top.jiangyixin.thea.sample.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jiangyixin.thea.core.TheaSsoClient;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.filter.SsoWebFilter;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/14 上午11:14
 */
@Configuration
public class TheaConfig {
	@Value("${thea.sso.server}")
	private String ssoServer;
	@Value("${thea.sso.redis-address}")
	private String ssoRedisAddress;
	@Value("${thea.sso.excluded-path}")
	private String ssoExcludedPath;
	@Value("${thea.sso.logout-path}")
	private String ssoLogoutPath;
	
	@Bean
	public FilterRegistrationBean<SsoWebFilter> ssoWebFilterRegistration() {
		TheaSsoClient.init(ssoRedisAddress);
		
		// filter init
		FilterRegistrationBean<SsoWebFilter> registration = new FilterRegistrationBean<>();
		
		// 设置过滤器名称
		registration.setName("XxlSsoWebFilter");
		// 设置过滤器优先级，越低越优先，最顶级为1
		registration.setOrder(1);
		// 过滤所有路径
		registration.addUrlPatterns("/*");
		// 注册自定义过滤器
		registration.setFilter(new SsoWebFilter());
		// 配置初始化参数
		registration.addInitParameter(SsoConfig.SSO_SERVER, ssoServer);
		registration.addInitParameter(SsoConfig.SSO_LOGOUT_PATH, ssoLogoutPath);
		registration.addInitParameter(SsoConfig.SSO_EXCLUDED_PATHS, ssoExcludedPath);
		return registration;
	}
}
