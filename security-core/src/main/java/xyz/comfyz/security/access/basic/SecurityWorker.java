package xyz.comfyz.security.access.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.comfyz.security.access.SecurityFilter;
import xyz.comfyz.security.cache.CacheStorager;
import xyz.comfyz.security.model.TokenConfig;
import xyz.comfyz.security.provider.TokenProvider;
import xyz.comfyz.security.provider.token.TokenReader;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:44 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class SecurityWorker {

    @SuppressWarnings("unchecked")
    @Autowired
    public SecurityWorker(WebSecurityConfigAdapter webSecurityConfigAdapter, HttpSecurity httpSecurity,
                          SecurityFilter securityFilter, SecurityMetadataSource securityMetadataSource,
                          TokenProvider tokenProvider) {
        webSecurityConfigAdapter.config(httpSecurity);
        securityFilter.setMatcher(httpSecurity.filterPath());
        securityMetadataSource.authorized(httpSecurity.authorizations());
        securityMetadataSource.exclude(httpSecurity.exclusions());

        final TokenConfig tokenConfig = httpSecurity.tokenConfig();
        tokenProvider.config(tokenConfig);
        if (httpSecurity.enableCookie())
            tokenProvider.enableCookie(httpSecurity.debug());

        tokenProvider.add(httpSecurity.tokenCache().toArray(new CacheStorager[0]));
        tokenProvider.add(httpSecurity.tokenReader().toArray(new TokenReader[0]));
    }

}
