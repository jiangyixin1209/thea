package top.jiangyixin.thea.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.helper.SsoLoginStoreHelper;
import top.jiangyixin.thea.core.helper.SsoUserHelper;
import top.jiangyixin.thea.core.service.SsoTokenService;
import top.jiangyixin.thea.server.pojo.vo.R;
import top.jiangyixin.thea.server.pojo.dto.UserDTO;
import top.jiangyixin.thea.server.service.UserService;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * TokenController
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:04
 */
@RestController
@RequestMapping("/token")
public class TokenController {
	
	@Resource
	private UserService userService;
	
	@PostMapping("/login")
	public R<String> login(String username, String password) {
		UserDTO userDTO = userService.loadUser(username, password);
		if (userDTO == null) {
			return R.fail("SSO未登录");
		}
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(String.valueOf(userDTO.getUserId()));
		ssoUser.setUsername(userDTO.getUsername());
		ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("_", ""));
		ssoUser.setExpireMinute(SsoLoginStoreHelper.getRedisExpireMinute());
		ssoUser.setLastFreshTime(System.currentTimeMillis());
		
		String sessionId = SsoUserHelper.makeSessionId(ssoUser);
		SsoTokenService.login(sessionId, ssoUser);
		return new R<>(R.SUCCESS_CODE, sessionId);
	}
	
	@PostMapping("/logout")
	public R<String> logout(String sessionId) {
		SsoTokenService.logout(sessionId);
		return R.success();
	}
	
	@PostMapping("/loadUser")
	public R<SsoUser> loadUser(String sessionId) {
		SsoUser ssoUser = SsoTokenService.loadUser(sessionId);
		if (ssoUser == null) {
			return new R<>(R.ERROR_CODE, "SSO未登录");
		}
		return new R<>(R.SUCCESS_CODE, ssoUser);
	}
	
}
