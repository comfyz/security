package xyz.comfyz.security.core.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.access.SecurityFilter;
import xyz.comfyz.security.core.cache.UserDetailsCache;
import xyz.comfyz.security.core.support.SecurityAuthorizeMode;
import xyz.comfyz.security.core.support.SecurityMetadataSource;
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
public class HttpSecurity {
    private static CookieConfig cookieConfig;
    private final AntMatchers antMatchers;
    private final SecurityFilter securityFilter;
    private String[] pathPatterns;

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

    public void filter(String pattern) {
        if (StringUtils.hasText(pattern))
            securityFilter.setMatcher(new AntPathRequestMatcher(pattern));
    }

    public AntMatchers antMatchers(AntPathRequestMatcher... matcher) {
        return this.antMatchers.antMatchers(matcher);
    }

    public AntMatchers antMatchers(String... matcher) {
        return this.antMatchers.antMatchers(matcher);
    }

    public HttpSecurity userDetailsCache(UserDetailsCache userDetailsCache) {
        this.securityFilter.userDetailsCache(userDetailsCache);
        return this;
    }

    public HttpSecurity cookie(String domain) {
        cookieConfig = new CookieConfig(domain);
        return this;
    }

    public HttpSecurity cookie(String name, String domain, String path, Integer expiry) {
        cookieConfig = new CookieConfig(name, domain, path, expiry);
        return this;
    }

    public class AntMatchers {
        private final SecurityMetadataSource metadataSource;
        private Set<AntPathRequestMatcher> matchers = new HashSet<>();
        private HttpSecurity httpSecurity;

        AntMatchers(SecurityMetadataSource metadataSource, HttpSecurity httpSecurity) {
            this.metadataSource = metadataSource;
            this.httpSecurity = httpSecurity;
        }

        public HttpSecurity exclude() {
            antMatchers.matchers.add(new AntPathRequestMatcher("/error"));
            metadataSource.addAll(antMatchers.matchers.parallelStream().collect(Collectors.toMap(Function.identity(), matcher -> SecurityAuthorizeMode.NONE)));
            antMatchers.clear();
            return this.httpSecurity;
        }

        public HttpSecurity authorized() {
            if (!antMatchers.matchers.isEmpty())
                metadataSource.addAll(antMatchers.matchers.parallelStream().collect(Collectors.toMap(Function.identity(), matcher -> SecurityAuthorizeMode.AUTHENTICATED)));
            antMatchers.clear();
            return this.httpSecurity;
        }

        Set<AntPathRequestMatcher> mathcers() {
            return this.matchers;
        }

        public AntMatchers antMatchers(AntPathRequestMatcher... matcher) {
            this.matchers.addAll(Arrays.asList(matcher));
            return this;
        }

        public AntMatchers antMatchers(String... matcher) {
            this.matchers.addAll(Arrays.stream(matcher).filter(StringUtils::hasText).map(AntPathRequestMatcher::new).collect(Collectors.toList()));
            return this;
        }

        void clear() {
            this.matchers.clear();
        }

    }

    public class CookieConfig {
        private final String name;
        private final String domain;
        private final String path;
        private final int expiry;

        public CookieConfig() {
            this(null, null, null, null);
        }

        public CookieConfig(String domain) {
            this(null, domain, null, null);
        }

        public CookieConfig(String name, String domain, String path, Integer expiry) {
            this.name = StringUtils.hasText(name) ? name : defaultName();
            this.domain = StringUtils.hasText(domain) ? domain : defaultDomain();
            this.path = StringUtils.hasText(path) ? path : defaultPath();
            this.expiry = expiry != null && expiry > 0 ? expiry : defaultExpiry();
        }

        public String getName() {
            return name;
        }

        public String getDomain() {
            return domain;
        }

        public String getPath() {
            return path;
        }

        public int getExpiry() {
            return expiry;
        }

        public String defaultName() {
            return "security_token";
        }

        public String defaultDomain() {
            return "comfyz.xyz";
        }

        public String defaultPath() {
            return "/";
        }

        public int defaultExpiry() {
            return 60 * 15;
        }

    }

}
