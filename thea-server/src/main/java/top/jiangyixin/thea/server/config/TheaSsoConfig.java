package top.jiangyixin.thea.server.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import top.jiangyixin.thea.core.util.JedisUtils;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:01
 */
@Configuration
public class TheaSsoConfig implements InitializingBean, DisposableBean {
	
	@Value("${thea.sso.redis.address}")
	private String redisAddress;
	
	@Override
	public void destroy() throws Exception {
		JedisUtils.close();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		JedisUtils.init(redisAddress);
	}
}
