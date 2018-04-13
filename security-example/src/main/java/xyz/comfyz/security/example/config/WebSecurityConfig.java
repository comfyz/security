package xyz.comfyz.security.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.comfyz.security.EnableWebSecurity;
import xyz.comfyz.security.access.basic.HttpSecurity;
import xyz.comfyz.security.access.basic.WebSecurityConfigAdapter;
import xyz.comfyz.security.cache.storager.FileCacheStorager;
import xyz.comfyz.security.example.service.UserDetailsServiceImpl;
import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.provider.UserDetailsService;
import xyz.comfyz.security.provider.auth.impl.NullAuthenticationCache;
import xyz.comfyz.security.provider.token.impl.RequestHeaderTokenReader;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        13:28 2018/3/27
 * Version:     1.0
 * Description: 默认访问需要登录，并且具有对应资源的访问权限 {@link Authentication 登录用户信息及权限实体}
 * 继承{@link WebSecurityConfigAdapter} 自定义拦截设置
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigAdapter {

    /**
     * {@link UserDetailsServiceImpl} 实现 {@link UserDetailsService}
     * 提供一个根据userId加载 {@link Authentication 用户信息}的实现，对cookie中存放的user进行提取
     */

    @Override
    protected void config(HttpSecurity httpSecurity) {
        httpSecurity
                //token设置
                .tokenConfig("Authorization", 60 * 30)

                //启用cookie
//                .enableCookie("comfyz.xyz", "/", false)

                //公开
                .open("/login/**", "/error", "/swagger-ui.html", "/swagger-resources/**")

                //登录即可访问
                .authorized("/auth/**", "/v2/api-docs")

                //拦截路径
                .filter("/**")

                //读取token方式
                .reader(new RequestHeaderTokenReader())

                .cache(new FileCacheStorager<>());
    }

    @Bean
    public NullAuthenticationCache nullUserDetailsCache() {
        return new NullAuthenticationCache();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

}
