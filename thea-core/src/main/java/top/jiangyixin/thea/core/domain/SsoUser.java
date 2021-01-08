package top.jiangyixin.thea.core.domain;

import java.io.Serializable;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/6 下午3:29
 */
public class TheaSsoUser implements Serializable {
	private static final long serialVersionUID = 42L;
	private String userId;
	private String username;
	private String version;
	
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
	
	@Override
	public String toString() {
		return "TheaSsoUser{" +
				"userId='" + userId + '\'' +
				", username='" + username + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
