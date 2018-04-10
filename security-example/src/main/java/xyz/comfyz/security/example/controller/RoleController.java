package xyz.comfyz.security.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.comfyz.security.SecurityContext;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        17:25 2018/3/28
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @GetMapping("zhangsan")
    public String zhangsan() {
        return "Hi, " + SecurityContext.authenticationToken().getUserDetails().getUserName() + ". This path was only access for 张三";
    }

    @GetMapping("/zhangsan/hi")
    public String zhangsanHi() {
        return "Hi, " + SecurityContext.authenticationToken().getUserDetails().getUserName();
    }

    @GetMapping("lisi")
    public String lisi() {
        return "Hi, " + SecurityContext.authenticationToken().getUserDetails().getUserName() + ". This path was only access for 李四";
    }

    @GetMapping("/lisi/hi")
    public String lisiHi() {
        return "Hi, " + SecurityContext.authenticationToken().getUserDetails().getUserName();
    }
}
