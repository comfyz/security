package xyz.comfyz.security.provider;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.cache.CacheStorager;
import xyz.comfyz.security.model.Token;
import xyz.comfyz.security.model.TokenConfig;
import xyz.comfyz.security.provider.token.TokenReader;
import xyz.comfyz.security.provider.token.impl.CookieTokenReader;
import xyz.comfyz.security.provider.token.impl.CookieTokenWriter;
import xyz.comfyz.security.support.SecurityContext;
import xyz.comfyz.security.util.EncryptUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : comfy
 * @date : 2018-04-03 14:19
 */
@Component
public final class TokenProvider {

    private final static TreeSet<TokenReader> readers = new TreeSet<>();
    private final static Set<CacheStorager<Token>> caches = new HashSet<>();
    private static CookieTokenWriter cookieTokenWriter;
    private static CookieTokenReader cookieTokenReader;
    private static TokenConfig tokenConfig;
    private static boolean enableCookie = false;
    private static boolean debug = false;

    public static String get() {
        if (!StringUtils.hasText(SecurityContext.token())) {
            AtomicReference<String> secret = new AtomicReference<>();
            if (enableCookie)
                secret.set(cookieTokenReader.read(tokenConfig.getName()));
            if (!StringUtils.hasText(secret.get()))
                readers.stream()
                        .map(reader -> reader.read(tokenConfig.getName()))
                        .filter(StringUtils::hasText)
                        .filter(s ->
                                caches.isEmpty() || caches.parallelStream().anyMatch(cache -> cache.lookup(EncryptUtil.md5(s)) != null))
                        .findFirst().ifPresent(secret::set);
            if (StringUtils.hasText(secret.get())) {
                SecurityContext.set(secret.get());
                TokenProvider.flush(secret.get());
            }
        }
        return SecurityContext.token();
    }

    public static void flush(String secret) {
        if (StringUtils.hasText(secret)) {
            if (enableCookie)
                cookieTokenWriter.write(tokenConfig.getDomain(), tokenConfig.getExpiry(), tokenConfig.getPath(), tokenConfig.getName(), secret, debug);
            caches.forEach(tokenCache -> tokenCache.caching(EncryptUtil.md5(secret), new Token(System.currentTimeMillis(), tokenConfig.getExpiry() * 1000)));
        }
    }

    public static void remove(String secret) {
        if (StringUtils.hasText(secret)) {
            caches.forEach(tokenCache -> tokenCache.evict(secret));
        }
    }

    public void add(TokenReader... reader) {
        readers.addAll(Arrays.asList(reader));
    }

    public void add(CacheStorager<Token>... writer) {
        caches.addAll(Arrays.asList(writer));
    }


    public void enableCookie(boolean debug) {
        cookieTokenReader = new CookieTokenReader();
        cookieTokenWriter = new CookieTokenWriter();
        enableCookie = true;
        TokenProvider.debug = debug;
    }

    public void config(TokenConfig tokenConfig) {
        TokenProvider.tokenConfig = tokenConfig;
    }
}
