package xyz.comfyz.security.core.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.cache.UserDetailsCache;
import xyz.comfyz.security.core.common.SecurityContext;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:11 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public class AuthenticationTokenProvider {
    private boolean ennableCache = false;
    private UserDetailsCache userDetailsCache;
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthenticationToken loadUser(HttpServletRequest request) {
        //上下文查找
        AuthenticationToken token = SecurityContext.getAuthenticationToken();

        if (token == null) {
            //检查cookie
            String userId = SecurityUtils.getUserId();
            if (!StringUtils.hasText(userId))
                return null;

            //session
            token = SecurityUtils.getSessionToken();
            if (token == null) {
                if (this.ennableCache) {
                    //自定义缓存
                    token = this.userDetailsCache.get(userId);
                }
                if (token == null)
                    //查库获取
                    token = this.userDetailsService.loadUser(userId);
            }
        }

        //放入缓存
        cacheToken(token);

        return token;
    }

    private void cacheToken(AuthenticationToken token) {
        if (token != null) {
            SecurityContext.set(token);
            if (this.ennableCache)
                this.userDetailsCache.put(token);
        }
    }

    public void userDetailsCache(UserDetailsCache userDetailsCache) {
        this.userDetailsCache = userDetailsCache;
        this.ennableCache = userDetailsCache != null;
    }

}