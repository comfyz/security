package xyz.comfyz.security.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.util.SecurityUtils;
import xyz.comfyz.security.example.service.UserDetailsServiceImpl;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        17:27 2018/3/28
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/{userId}")
    public String admin(@PathVariable String userId) {
        SecurityUtils.signOut();
        AuthenticationToken token = userDetailsService.loadUser(userId);
        if (token == null) {
            return "no user found";
        }
        SecurityUtils.signIn(token);
        return "login success, hi " + token.getUserDetails().getUserName();
    }

    @GetMapping("out")
    public String loginOut() {
        SecurityUtils.signOut();
        return "Login out success.";
    }

}
