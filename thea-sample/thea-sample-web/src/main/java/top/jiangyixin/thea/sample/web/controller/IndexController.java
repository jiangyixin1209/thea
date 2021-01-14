package top.jiangyixin.thea.sample.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.jiangyixin.thea.core.common.SsoConfig;
import top.jiangyixin.thea.core.domain.SsoUser;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 * @version 1.0
 * @author jiangyixin
 * @date 2021/1/14 下午3:55
 */
@Controller
public class IndexController {
	
	@GetMapping("/")
	public String index(Model model, HttpServletRequest request) {
		
		SsoUser ssoUser = (SsoUser) request.getAttribute(SsoConfig.SSO_USER);
		model.addAttribute("ssoUser", ssoUser);
		return "index";
	}
	
	@GetMapping("/json")
	@ResponseBody
	public SsoUser json(Model model, HttpServletRequest request) {
		SsoUser ssoUser = (SsoUser) request.getAttribute(SsoConfig.SSO_USER);
		model.addAttribute("ssoUser", ssoUser);
		return ssoUser;
	}
}
