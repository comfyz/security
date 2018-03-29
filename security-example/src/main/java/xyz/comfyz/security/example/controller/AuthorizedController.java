package xyz.comfyz.security.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.comfyz.security.core.common.SecurityContext;

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

    @GetMapping
    public String sayHi() {
        return "Hi, " + SecurityContext.getAuthenticationToken().getUserDetails().getUserName() + ", u have login";
    }

}
