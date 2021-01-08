package top.jiangyixin.thea.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import top.jiangyixin.thea.core.exception.TheaException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Jedis工具类
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 上午11:23
 */
public class JedisUtils {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	private static String address;
	private static ShardedJedisPool shardedJedisPool;
	private static final ReentrantLock INIT_LOCK = new ReentrantLock(false);
	
	public static void init(String address) {
		JedisUtils.address = address;
		getInstance();
	}
	
	/**
	 * 初始化ShardedJedisPool并获取ShardedJedis实例
	 * @return  ShardedJedis
	 */
	public static ShardedJedis getInstance() {
		if (shardedJedisPool == null) {
			try{
				if (INIT_LOCK.tryLock(2, TimeUnit.SECONDS)) {
					try{
						if (shardedJedisPool == null) {
							// JedisPoolConfig
							JedisPoolConfig config = new JedisPoolConfig();
							config.setMaxTotal(200);
							config.setMaxIdle(50);
							config.setMinIdle(8);
							// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted)
							// 如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
							config.setMaxWaitMillis(10000);
							// 在获取连接的时候检查有效性, 默认false
							config.setTestOnBorrow(true);
							// 调用returnObject方法时，是否进行有效检查
							config.setTestOnReturn(false);
							// Idle时进行连接扫描
							config.setTestWhileIdle(true);
							// 表示idle object eviction两次扫描之间要sleep的毫秒数
							config.setTimeBetweenEvictionRunsMillis(30000);
							// 表示idle object eviction每次扫描的最多的对象数
							config.setNumTestsPerEvictionRun(10);
							// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object eviction扫描并驱逐；
							// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
							config.setMinEvictableIdleTimeMillis(60000);
							
							// JedisShardInfo List
							List<JedisShardInfo> jedisShardInfos = new LinkedList<JedisShardInfo>();
							
							String[] addressArr = address.split(",");
							for (String s : addressArr) {
								JedisShardInfo jedisShardInfo = new JedisShardInfo(s);
								jedisShardInfos.add(jedisShardInfo);
							}
							shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
							logger.info("JedisUtils.ShardedJedisPool init success.");
						}
					} finally{
						INIT_LOCK.unlock();
					}
				}
			} catch (InterruptedException e) {
				logger.error("ShardedJedisPool Init error, {}", e.getMessage());
			}
		}
		if (shardedJedisPool == null) {
			throw new TheaException("JedisUtil.ShardedJedisPool init fail, shardedJedisPool is null");
		}
		return shardedJedisPool.getResource();
	}
	
	/**
	 * 关闭ShardedJedisPool线程池
	 * @throws IOException  IOException
	 */
	public static void close() throws IOException {
		if(shardedJedisPool != null) {
			shardedJedisPool.close();
		}
	}
	
	/**
	 * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
	 *
	 * @param object    object
	 * @return  byte[]
	 */
	private static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * 反序列化将byte[] -->Object
	 *
	 * @param bytes byte[]
	 * @return  Object
	 */
	private static Object deserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * Set String
	 *
	 * @param key   Redis Key
	 * @param value String value
	 * @param seconds 存活时间,单位/秒
	 * @return  String
	 */
	public static String setStringValue(String key, String value, int seconds) {
		String result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.setex(key, seconds, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * Set Object
	 *
	 * @param key   Redis Key
	 * @param obj   Object
	 * @param seconds 存活时间,单位/秒
	 */
	public static String setObjectValue(String key, Object obj, int seconds) {
		String result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.setex(key.getBytes(), seconds, serialize(obj));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * Get String
	 *
	 * @param key   Redis Key
	 * @return      String value
	 */
	public static String getStringValue(String key) {
		String value = null;
		try (ShardedJedis client = getInstance()) {
			value = client.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}
	
	/**
	 * Get Object
	 *
	 * @param key   Redis Key
	 * @return  Object
	 */
	public static Object getObjectValue(String key) {
		Object obj = null;
		try (ShardedJedis client = getInstance()) {
			byte[] bytes = client.get(key.getBytes());
			if (bytes != null && bytes.length > 0) {
				obj = deserialize(bytes);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return obj;
	}
	
	/**
	 * Delete key
	 *
	 * @param key   Redis key
	 * @return Integer reply, specifically:
	 * an integer greater than 0 if one or more keys were removed
	 * 0 if none of the specified key existed
	 */
	public static Long del(String key) {
		Long result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * incrBy i(+i)
	 *
	 * @param key   Redis
	 * @param i     步长
	 * @return 增长后的值
	 */
	public static Long incrBy(String key, int i) {
		Long result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.incrBy(key, i);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * exists valid
	 *
	 * @param key   Redis Key
	 * @return key存在返回true否则返回false
	 */
	public static Boolean exists(String key) {
		Boolean result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.exists(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * expire reset
	 *
	 * @param key   Redis key
	 * @param seconds 存活时间,单位/秒
	 * @return Integer reply, specifically:
	 * 1: the timeout was set.
	 * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
	 */
	public static Long expire(String key, int seconds) {
		Long result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.expire(key, seconds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * expire at unixTime
	 *
	 * @param key   Redis key
	 * @param unixTime  时间戳
	 * @return  Long
	 */
	public static Long expireAt(String key, long unixTime) {
		Long result = null;
		try (ShardedJedis client = getInstance()) {
			result = client.expireAt(key, unixTime);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
}
