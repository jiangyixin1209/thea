package top.jiangyixin.thea.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;
import top.jiangyixin.thea.core.helper.SsoLoginStoreHelper;
import top.jiangyixin.thea.core.helper.SsoUserHelper;
import top.jiangyixin.thea.core.service.SsoWebService;
import top.jiangyixin.thea.core.util.StringUtils;
import top.jiangyixin.thea.server.pojo.dto.UserDTO;
import top.jiangyixin.thea.server.pojo.param.LoginParam;
import top.jiangyixin.thea.server.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/8 下午5:04
 */
@Controller
public class WebController {
	@Resource
	private UserService userService;
	
	@GetMapping("/")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		SsoUser ssoUser = SsoWebService.loadUser(request, response);
		if (ssoUser == null) {
			return "redirect:/login";
		}
		model.addAttribute("ssoUser", ssoUser);
		return "index";
	}
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		SsoUser ssoUser = SsoWebService.loadUser(request, response);
		if (ssoUser != null) {
			String redirectUrl = request.getParameter(SsoConfig.REDIRECT_URL);
			if (StringUtils.isNotEmpty(redirectUrl)) {
				String sessionId = SsoUserHelper.makeSessionId(ssoUser);
				String finalRedirectUrl = redirectUrl.concat("?")
						.concat(SsoConfig.SSO_SESSION_ID)
						.concat("=").concat(sessionId);
				return "redirect:".concat(finalRedirectUrl);
			} else {
				return "redirect:/";
			}
		}
		return "login";
		
	}
	
	@PostMapping("/login")
	public String doLogin(HttpServletRequest request, HttpServletResponse response,
	                    RedirectAttributes redirectAttributes, LoginParam loginParam) {
		boolean isRemember = "on".equals(loginParam.getIsRemember());
		UserDTO userDTO = userService.loadUser(loginParam.getUsername(), loginParam.getPassword());
		if (userDTO == null) {
			return "redirect:/login";
		}
		SsoUser ssoUser = new SsoUser();
		ssoUser.setUserId(String.valueOf(userDTO.getUserId()));
		ssoUser.setUsername(ssoUser.getUsername());
		ssoUser.setVersion(UUID.randomUUID().toString().replaceAll("-", ""));
		ssoUser.setExpireMinute(SsoLoginStoreHelper.getRedisExpireMinute());
		ssoUser.setLastFreshTime(System.currentTimeMillis());
		
		String sessionId = SsoUserHelper.makeSessionId(ssoUser);
		SsoWebService.login(response, sessionId, ssoUser, isRemember);
		
		String redirectUrl = request.getParameter(SsoConfig.REDIRECT_URL);
		if (StringUtils.isNotEmpty(redirectUrl)) {
			String finalRedirectUrl = redirectUrl.concat("?").concat(SsoConfig.SSO_SESSION_ID)
					.concat("=").concat(sessionId);
			return "redirect:".concat(finalRedirectUrl);
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response,
	                     RedirectAttributes redirectAttributes) {
		SsoWebService.logout(request, response);
		redirectAttributes.addAttribute(SsoConfig.REDIRECT_URL, request.getParameter(SsoConfig.REDIRECT_URL));
		return "redirect:/login";
	}
 }
