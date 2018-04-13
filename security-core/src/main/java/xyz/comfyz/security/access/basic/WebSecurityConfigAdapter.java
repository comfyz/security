package xyz.comfyz.security.access.basic;

import xyz.comfyz.security.cache.storager.FileCacheStorager;
import xyz.comfyz.security.provider.token.impl.RequestHeaderTokenReader;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        13:22 2018/3/27
 * Version:     1.0
 * Description:
 */
public abstract class WebSecurityConfigAdapter {

    protected void config(HttpSecurity httpSecurity) {
        httpSecurity
                //token设置
                .tokenConfig("Authorization", 60 * 30)

                //启用cookie
                .enableCookie("comfyz.xyz", "/", false)

                //公开
                .open("/login/**", "/error", "/swagger-ui.html", "/swagger-resources/**")

                //登录即可访问
                .authorized("/v2/api-docs")

                //拦截路径
                .filter("/**")

                //读取token方式
                .reader(new RequestHeaderTokenReader())

                .cache(new FileCacheStorager<>());
    }

}
