package xyz.comfyz.security.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.comfyz.security.example.bo.Auth;
import xyz.comfyz.security.example.bo.User;
import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.support.SecurityContext;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        17:25 2018/3/28
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping("auth")
public class AuthorizedController {

    @GetMapping("hi")
    public String sayHi() {
        return "Hi, " + SecurityContext.authenticationToken().getUserDetails().getUserName() + ", u have login";
    }

    @GetMapping
    public String sayHello() {
        return "Hello, " + SecurityContext.authenticationToken().getUserDetails().getUserName() + ", u have login";
    }

    @SuppressWarnings("unchecked")
    @GetMapping("info")
    public AuthenticationToken<User, Auth> token() {
        return SecurityContext.authenticationToken();
    }

}
