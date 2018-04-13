package xyz.comfyz.security.access.basic;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.cache.CacheStorager;
import xyz.comfyz.security.model.Token;
import xyz.comfyz.security.model.TokenConfig;
import xyz.comfyz.security.provider.token.TokenReader;
import xyz.comfyz.security.support.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:07 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class HttpSecurity {

    private final TokenConfig tokenConfig = new TokenConfig();
    private final AtomicReference<String> filterPath = new AtomicReference<>(null);
    private final Set<AntPathRequestMatcher> authorizations = new HashSet<>();
    private final Set<AntPathRequestMatcher> exclusions = new HashSet<>();
    private final TreeSet<TokenReader> readers = new TreeSet<>();
    private final Set<CacheStorager<Token>> caches = new HashSet<>();
    private boolean enableCookie = false;
    private boolean debug = false;

    TokenConfig tokenConfig() {
        return this.tokenConfig;
    }

    boolean enableCookie() {
        return this.enableCookie;
    }

    boolean debug() {
        return this.debug;
    }

    String filterPath() {
        return this.filterPath.get();
    }

    Set<AntPathRequestMatcher> exclusions() {
        return this.exclusions;
    }

    Set<AntPathRequestMatcher> authorizations() {
        return this.authorizations;
    }

    TreeSet<TokenReader> tokenReader() {
        return this.readers;
    }

    Set<CacheStorager<Token>> tokenCache() {
        return this.caches;
    }

    /*********************** token 配置 *****************************/
    public HttpSecurity tokenConfig(String name, Integer expiry) {
        this.tokenConfig.setName(name).setExpiry(expiry);
        return this;
    }

    public HttpSecurity enableCookie(String domain, String path, boolean debug) {
        this.enableCookie = true;
        this.debug = debug;
        this.tokenConfig.setDomain(domain).setPath(path);

        return this;
    }

    /*********************** filter 配置 *****************************/
    public HttpSecurity filter(String pattern) {
        if (StringUtils.hasText(pattern))
            this.filterPath.set(pattern);
        return this;
    }

    /*********************** metaSources 路径权限 配置 *****************************/
    public HttpSecurity open(String... matcher) {
        Arrays.stream(matcher).filter(StringUtils::hasText).distinct().map(AntPathRequestMatcher::new).forEach(this.exclusions::add);
        return this;
    }

    public HttpSecurity authorized(String... matcher) {
        Arrays.stream(matcher).filter(StringUtils::hasText).distinct().map(AntPathRequestMatcher::new).forEach(this.authorizations::add);
        return this;
    }

    /*********************** token获取方式 配置 *****************************/
    public HttpSecurity reader(TokenReader... reader) {
        this.readers.addAll(Arrays.asList(reader));
        return this;
    }

    /*********************** token缓存 配置 *****************************/
    @SafeVarargs
    public final HttpSecurity cache(CacheStorager<Token>... cache) {
        this.caches.addAll(Arrays.asList(cache));
        return this;
    }

}
