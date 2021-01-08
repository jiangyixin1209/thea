package top.jiangyixin.thea.core.domain;

import java.io.Serializable;

/**
 * SsoUser
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:29
 */
public class SsoUser implements Serializable {
	private static final long serialVersionUID = 42L;
	private String userId;
	private String username;
	private String version;
	private long expireMinute;
	private long expireFreshTime;
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public long getExpireMinute() {
		return expireMinute;
	}
	
	public void setExpireMinute(long expireMinute) {
		this.expireMinute = expireMinute;
	}
	
	public long getExpireFreshTime() {
		return expireFreshTime;
	}
	
	public void setExpireFreshTime(long expireFreshTime) {
		this.expireFreshTime = expireFreshTime;
	}
	
	@Override
	public String toString() {
		return "SsoUser{" +
				"userId='" + userId + '\'' +
				", username='" + username + '\'' +
				", version='" + version + '\'' +
				", expireMinute=" + expireMinute +
				", expireFreshTime=" + expireFreshTime +
				'}';
	}
	
}
