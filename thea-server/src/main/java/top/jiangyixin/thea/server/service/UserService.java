package top.jiangyixin.thea.server.service;

import top.jiangyixin.thea.server.pojo.vo.R;
import top.jiangyixin.thea.server.pojo.dto.UserDTO;

/**
 * UserService
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:25
 */
public interface UserService {
	
	/**
	 * 获取用户
	 *
	 * 获取用户的用户信息
	 * @param username  用户名
	 * @param password  密码
	 * @return  UserDTO
	 */
	R<UserDTO> loadUser(String username, String password);
}
