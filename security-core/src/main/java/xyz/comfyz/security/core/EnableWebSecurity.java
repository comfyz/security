package xyz.comfyz.security.core;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/26
 * Version:     1.0
 * Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan({"xyz.comfyz.security.core"})
public @interface EnableWebSecurity {
}