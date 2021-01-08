package top.jiangyixin.thea.server.service.impl;

import org.springframework.stereotype.Service;
import top.jiangyixin.thea.server.pojo.vo.R;
import top.jiangyixin.thea.server.pojo.dto.UserDTO;
import top.jiangyixin.thea.server.service.UserService;

/**
 * UserServiceImpl
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:30
 */
@Service
public class UserServiceImpl implements UserService {
	@Override
	public R<UserDTO> loadUser(String username, String password) {
		// 模拟根据用户名和密码获取用户
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(1);
		userDTO.setPassword("123");
		userDTO.setUsername("admin");
		return new R<>(R.SUCCESS_CODE, userDTO);
	}
}
