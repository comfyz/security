package xyz.comfyz.security.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.comfyz.security.core.EnableWebSecurity;
import xyz.comfyz.security.core.access.basic.HttpSecurity;
import xyz.comfyz.security.core.access.basic.WebSecurityConfigAdapter;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.provider.UserDetailsService;
import xyz.comfyz.security.core.provider.auth.impl.NullAuthenticationTokenCache;
import xyz.comfyz.security.example.service.UserDetailsServiceImpl;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        13:28 2018/3/27
 * Version:     1.0
 * Description: 默认访问需要登录，并且具有对应资源的访问权限 {@link AuthenticationToken 登录用户信息及权限实体}
 * 继承{@link WebSecurityConfigAdapter} 自定义拦截设置
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigAdapter {

    /**
     * {@link UserDetailsServiceImpl} 实现 {@link UserDetailsService}
     * 提供一个根据userId加载 {@link AuthenticationToken 用户信息}的实现，对cookie中存放的user进行提取
     */

    @Override
    protected void config(HttpSecurity httpSecurity) {

        httpSecurity
                //token设置
                .tokenConfig("Authorization", 60 * 30)
                //启用cookie
//                .enableCookie(null, "/")
                //公开
                .open("/login/**", "/error", "/api/**", "/swagger-ui.html", "/swagger-resources/**")
                //登录即可访问
                .authorized("/auth/**", "/v2/api-docs")
                //拦截路径
                .filter("/**");
    }

    @Bean
    public NullAuthenticationTokenCache nullUserDetailsCache() {
        return new NullAuthenticationTokenCache();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

}
