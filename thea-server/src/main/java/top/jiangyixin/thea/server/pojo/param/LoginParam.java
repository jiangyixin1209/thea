package top.jiangyixin.thea.server.pojo.param;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/12 下午3:50
 */
public class LoginParam {
	private String username;
	private String password;
	private String isRemember;
	
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
	
	public String getIsRemember() {
		return isRemember;
	}
	
	public void setIsRemember(String isRemember) {
		this.isRemember = isRemember;
	}
}
