package xyz.comfyz.security.core.access.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:44 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public class SecurityWorker {

    private HttpSecurity httpSecurity;

    @Autowired
    public SecurityWorker(HttpSecurity httpSecurity, WebSecurityConfigAdapter webSecurityConfigAdapter) {
        this.httpSecurity = httpSecurity;
        webSecurityConfigAdapter.config(httpSecurity);
    }

    public HttpSecurity getHttpSecurity() {
        return httpSecurity;
    }

}
