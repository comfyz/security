package xyz.comfyz.security.core.access.basic;

import org.springframework.stereotype.Component;
import xyz.comfyz.security.core.access.SecurityFilter;
import xyz.comfyz.security.core.provider.TokenProvider;
import xyz.comfyz.security.core.provider.token.TokenCache;
import xyz.comfyz.security.core.provider.token.TokenReader;
import xyz.comfyz.security.core.provider.token.TokenWriter;
import xyz.comfyz.security.core.provider.token.impl.CookieTokenReader;
import xyz.comfyz.security.core.provider.token.impl.CookieTokenWriter;
import xyz.comfyz.security.core.provider.token.impl.HeaderTokenReader;
import xyz.comfyz.security.core.provider.token.impl.HeapTokenCache;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:44 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class SecurityWorker {

    public SecurityWorker(WebSecurityConfigAdapter webSecurityConfigAdapter, HttpSecurity httpSecurity,
                          SecurityFilter securityFilter, SecurityMetadataSource securityMetadataSource,
                          TokenProvider tokenProvider) {
        webSecurityConfigAdapter.config(httpSecurity);
        securityFilter.setMatcher(httpSecurity.filterPath());
        securityMetadataSource.authorized(httpSecurity.authorizations());
        securityMetadataSource.exclude(httpSecurity.exclusions());

        tokenProvider.add(new HeaderTokenReader(httpSecurity.tokenConfig().getName(), httpSecurity.tokenConfig().getExpiry()));
        tokenProvider.add(new HeapTokenCache(httpSecurity.tokenConfig().getName(), httpSecurity.tokenConfig().getExpiry()));

        if (httpSecurity.enableCookie()) {
            tokenProvider.add(new CookieTokenWriter(httpSecurity.tokenConfig().getName(), httpSecurity.tokenConfig().getExpiry(),
                    httpSecurity.tokenConfig().getDomain(), httpSecurity.tokenConfig().getPath()));
            tokenProvider.add(new CookieTokenReader(httpSecurity.tokenConfig().getName(), httpSecurity.tokenConfig().getExpiry(),
                    httpSecurity.tokenConfig().getDomain(), httpSecurity.tokenConfig().getPath()));
        }

        tokenProvider.add(httpSecurity.tokenWriter().toArray(new TokenWriter[0]));
        tokenProvider.add(httpSecurity.tokenReader().toArray(new TokenReader[0]));
        tokenProvider.add(httpSecurity.tokenCache().toArray(new TokenCache[0]));
    }

}
