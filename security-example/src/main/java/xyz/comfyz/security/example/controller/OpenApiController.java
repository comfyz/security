package xyz.comfyz.security.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        17:25 2018/3/28
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping
public class OpenApiController {

    @GetMapping
    public String hello() {
        return "Hello, everybody.";
    }

    @GetMapping("api/hi")
    public String hi() {
        return "Hi, api.";
    }

    @GetMapping("api/hiaha")
    public String hiaha() {
        return "Hi, api. aha~";
    }

    @GetMapping("api/hi/hi/hi")
    public String hi3() {
        return "Hi! Hi! Hi! api.";
    }

    @GetMapping("api2")
    public String hi2() {
        return "Hi, api2.";
    }
}
