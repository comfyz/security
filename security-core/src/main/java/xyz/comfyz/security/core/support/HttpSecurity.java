package xyz.comfyz.security.core.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.access.SecurityFilter;
import xyz.comfyz.security.core.access.basic.SecurityMetadataSource;
import xyz.comfyz.security.core.model.SecurityAuthorizeMode;
import xyz.comfyz.security.core.util.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:07 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class HttpSecurity {
    private static CookieConfig cookieConfig;
    private static String tokenName = "Authentication";
    private final AntMatchers antMatchers;
    private final SecurityFilter securityFilter;

    @Autowired
    public HttpSecurity(SecurityMetadataSource metadataSource, SecurityFilter securityFilter) {
        this.antMatchers = new AntMatchers(metadataSource, this);
        this.securityFilter = securityFilter;
        this.securityFilter.setMatcher(new AntPathRequestMatcher("/**"));
        cookieConfig = new CookieConfig();
    }

    public static CookieConfig getCookieConfig() {
        return cookieConfig;
    }

    public static String getTokenName() {
        return tokenName;
    }

    public HttpSecurity cookieConfig(String name, String domain, String path, Integer expiry) {
        cookieConfig = new CookieConfig(name, domain, path, expiry);
        return this;
    }

    public void filter(String pattern) {
        if (StringUtils.hasText(pattern))
            securityFilter.setMatcher(new AntPathRequestMatcher(pattern));
    }

    public AntMatchers antMatchers(String... matcher) {
        return this.antMatchers.antMatchers(matcher);
    }

    public HttpSecurity tokenName(String name) {
        if (StringUtils.hasText(name))
            tokenName = name;
        return this;
    }

    public final class AntMatchers {
        private final SecurityMetadataSource metadataSource;
        private Set<AntPathRequestMatcher> matchers = new HashSet<>();
        private HttpSecurity httpSecurity;

        AntMatchers(SecurityMetadataSource metadataSource, HttpSecurity httpSecurity) {
            this.metadataSource = metadataSource;
            this.httpSecurity = httpSecurity;
        }

        public HttpSecurity exclude() {
            antMatchers.matchers.add(new AntPathRequestMatcher("/error"));
            metadataSource.addAll(antMatchers.matchers.parallelStream()
                    .collect(Collectors.toMap(Function.identity(), matcher -> SecurityAuthorizeMode.NONE)));
            antMatchers.clear();
            return this.httpSecurity;
        }

        public HttpSecurity authorized() {
            if (!antMatchers.matchers.isEmpty())
                metadataSource.addAll(antMatchers.matchers.parallelStream()
                        .collect(Collectors.toMap(Function.identity(), matcher -> SecurityAuthorizeMode.AUTHENTICATED)));
            antMatchers.clear();
            return this.httpSecurity;
        }

        Set<AntPathRequestMatcher> mathcers() {
            return this.matchers;
        }

        public AntMatchers antMatchers(String... matcher) {
            this.matchers.addAll(Arrays.stream(matcher)
                    .filter(StringUtils::hasText)
                    .map(AntPathRequestMatcher::new)
                    .collect(Collectors.toList()));
            return this;
        }

        void clear() {
            this.matchers.clear();
        }

    }

}
