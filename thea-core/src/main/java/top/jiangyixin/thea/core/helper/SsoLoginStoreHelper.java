package top.jiangyixin.thea.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.util.JedisUtils;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 上午11:17
 */
public class SsoLoginStoreHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SsoLoginStoreHelper.class);
	
	/**
	 * redis 默认过期时间，默认1440分钟(24小时)
	 *
	 */
	private static int REDIS_EXPIRE_MINUTE = 1440;
	/**
	 * 最小过期时间，默认30分钟
	 */
	private static final int MIN_EXPIRE_MINUTE = 30;
	
	/**
	 * 获取存放进Redis中的key
	 * @param sessionId sessionId
	 * @return  Redis Key
	 */
	private static String redisKey(String sessionId) {
		return SsoConfig.SSO_SESSION_ID.concat("#").concat(sessionId);
	}
	
	/**
	 * 根据SessionId从Redis中获取User
	 * @param userId userId
	 * @return  User
	 */
	public static SsoUser get(String userId) {
		String redisKey = redisKey(userId);
		Object objectValue = JedisUtils.getObjectValue(redisKey);
		if (objectValue != null) {
			try{
				return (SsoUser) objectValue;
			} catch (Exception e) {
				logger.error("Load User From Redis Error, {}", e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * 从Redis中移除User
	 * @param userId userId
	 */
	public static void remove(String userId) {
		String redisKey = redisKey(userId);
		JedisUtils.del(redisKey);
	}
	
	/**
	 * 存储user
	 * @param userId  userId
	 * @param ossUser   user
	 */
	public static void put(String userId, SsoUser ossUser) {
		String redisKey = redisKey(userId);
		JedisUtils.setObjectValue(redisKey, ossUser, REDIS_EXPIRE_MINUTE * 60);
	}
	
	public static int getRedisExpireMinute() {
		return REDIS_EXPIRE_MINUTE;
	}
	
	public static void setRedisExpireMinute(int redisExpireMinute) {
		if(redisExpireMinute < MIN_EXPIRE_MINUTE) {
			redisExpireMinute = MIN_EXPIRE_MINUTE;
		}
		REDIS_EXPIRE_MINUTE = redisExpireMinute;
	}
}
