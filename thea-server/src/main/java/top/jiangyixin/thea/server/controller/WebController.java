package top.jiangyixin.thea.server.controller;

import org.springframework.stereotype.Controller;
import top.jiangyixin.thea.server.service.UserService;

import javax.annotation.Resource;

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
	
}
