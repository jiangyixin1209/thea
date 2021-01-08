package top.jiangyixin.thea.server.pojo.dto;

/**
 * User DTO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:26
 */
public class UserDTO {
	
	private Integer userId;
	private String username;
	private String password;
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "UserDTO{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
