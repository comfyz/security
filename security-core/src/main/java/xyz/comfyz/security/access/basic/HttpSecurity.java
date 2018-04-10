package xyz.comfyz.security.access.basic;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.model.TokenConfig;
import xyz.comfyz.security.provider.token.TokenCache;
import xyz.comfyz.security.provider.token.TokenReader;
import xyz.comfyz.security.provider.token.TokenWriter;
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
    private final TreeSet<TokenWriter> writers = new TreeSet<>();
    private final Set<TokenCache> caches = new HashSet<>();
    private boolean enableCookie = false;

    boolean enableCookie() {
        return enableCookie;
    }

    TokenConfig tokenConfig() {
        return tokenConfig;
    }

    String filterPath() {
        return filterPath.get();
    }

    Set<AntPathRequestMatcher> exclusions() {
        return exclusions;
    }

    Set<AntPathRequestMatcher> authorizations() {
        return authorizations;
    }

    TreeSet<TokenReader> tokenReader() {
        return readers;
    }

    TreeSet<TokenWriter> tokenWriter() {
        return writers;
    }

    Set<TokenCache> tokenCache() {
        return caches;
    }

    /*********************** token 配置 *****************************/
    public HttpSecurity tokenConfig(String name, Integer expiry) {
        tokenConfig.setName(name).setExpiry(expiry);
        return this;
    }

    public HttpSecurity enableCookie(String domain, String path) {
        enableCookie = true;
        tokenConfig.setDomain(domain).setPath(path);

        return this;
    }

    /*********************** filter 配置 *****************************/
    public HttpSecurity filter(String pattern) {
        if (StringUtils.hasText(pattern))
            filterPath.set(pattern);
        return this;
    }

    /*********************** metaSources 路径权限 配置 *****************************/
    public HttpSecurity open(String... matcher) {
        Arrays.stream(matcher).filter(StringUtils::hasText).distinct().map(AntPathRequestMatcher::new).forEach(exclusions::add);
        return this;
    }

    public HttpSecurity authorized(String... matcher) {
        Arrays.stream(matcher).filter(StringUtils::hasText).distinct().map(AntPathRequestMatcher::new).forEach(authorizations::add);
        return this;
    }

    /*********************** token获取方式 配置 *****************************/
    public HttpSecurity add(TokenWriter... writer) {
        writers.addAll(Arrays.asList(writer));
        return this;
    }


    /*********************** token写入方式 配置 *****************************/
    public HttpSecurity add(TokenReader... reader) {
        readers.addAll(Arrays.asList(reader));
        return this;
    }

    /*********************** token缓存方式 配置 *****************************/
    public HttpSecurity add(TokenCache... cache) {
        caches.addAll(Arrays.asList(cache));
        return this;
    }

}
