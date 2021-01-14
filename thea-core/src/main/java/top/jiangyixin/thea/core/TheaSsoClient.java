package top.jiangyixin.thea.core;

import top.jiangyixin.thea.core.util.JedisUtils;

import java.io.IOException;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 上午11:29
 */
public class TheaSsoClient {
	
	
	public static void init(String redisAddresses) {
		initJedis(redisAddresses);
	}
	
	public static void initJedis(String redisAddress) {
		JedisUtils.init(redisAddress);
	}
	
	public static void destroy() throws IOException {
		JedisUtils.close();
	}
}
