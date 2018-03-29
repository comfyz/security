package xyz.comfyz.security.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.comfyz.security.core.EnableWebSecurity;
import xyz.comfyz.security.core.cache.impl.NullUserDetailsCache;
import xyz.comfyz.security.core.common.HttpSecurity;
import xyz.comfyz.security.core.common.WebSecurityConfigAdapter;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.support.UserDetailsService;
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
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void config(HttpSecurity httpSecurity) {

        httpSecurity

                /**
                 * 公开访问
                 * { /* } 匹配直接下级 例如 { /love }
                 * { /** } 表示所有所属下级 例如 { /love/you }
                 * { /love/** } 表示{ /hi } 的所有所属下级 例如 { /love },{ /love/you },{ /love/you/forever }，但不包括{ /lovewith }
                 */

                .antMatchers("/login/**")
                .antMatchers("/error")
                .antMatchers("/api/**")
                .exclude()

                //登录即可访问
                .antMatchers("/auth/**").authorized()

                //启用自定义user缓存策略
                .userDetailsCache(nullUserDetailsCache())

                //cookie信息
                .cookie("auth_user_token", "comfyz.xyz", "/", 60 * 20);
    }

    @Bean
    public NullUserDetailsCache nullUserDetailsCache() {
        return new NullUserDetailsCache();
    }

}
