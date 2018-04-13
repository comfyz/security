package xyz.comfyz.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.model.UserDetalis;
import xyz.comfyz.security.provider.auth.AuthenticationCache;
import xyz.comfyz.security.support.SecurityContext;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:11 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final AuthenticationCache userDetailsCache;

    @Autowired
    public AuthenticationProvider(UserDetailsService userDetailsService, AuthenticationCache userDetailsCache) {
        this.userDetailsService = userDetailsService;
        this.userDetailsCache = userDetailsCache;
    }

    public Authentication authenticate(UserDetalis userDetalis) {
        if (userDetalis == null || !StringUtils.hasText(userDetalis.getUserId()))
            return null;

        Authentication token = SecurityContext.authentication();

        if (token == null)
            token = userDetailsCache.get(userDetalis.getUserId());

        if (token == null)
            token = userDetailsService.loadUser(userDetalis.getUserId());

        if (token != null) {
            SecurityContext.set(token);
            userDetailsCache.cache(token);
        }
        return token;
    }

}
