package xyz.comfyz.security.core.provider;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.SecurityContext;
import xyz.comfyz.security.core.provider.token.TokenCache;
import xyz.comfyz.security.core.provider.token.TokenReader;
import xyz.comfyz.security.core.provider.token.TokenWriter;

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
    private final static TreeSet<TokenWriter> writers = new TreeSet<>();
    private final static Set<TokenCache> caches = new HashSet<>();

    public static String get() {
        if (!StringUtils.hasText(SecurityContext.token())) {
            AtomicReference<String> secret = new AtomicReference<>();
            readers.stream()
                    .map(TokenReader::read)
                    .filter(StringUtils::hasText)
                    .filter(s -> caches.parallelStream().anyMatch(cache -> cache.exist(s)))
                    .findFirst().ifPresent(secret::set);
            SecurityContext.set(secret.get());
        }
        return SecurityContext.token();
    }

    public static void flush(String secret) {
        if (StringUtils.hasText(secret)) {
            caches.forEach(tokenCache -> tokenCache.write(secret));
            writers.forEach(tokenCache -> tokenCache.write(secret));
        }
    }

    public static void remove(String secret) {
        if (StringUtils.hasText(secret)) {
            caches.forEach(tokenCache -> tokenCache.remove(secret));
            writers.forEach(tokenCache -> tokenCache.remove(secret));
        }
    }

    public void add(TokenReader... reader) {
        readers.addAll(Arrays.asList(reader));
    }

    public void add(TokenWriter... writer) {
        writers.addAll(Arrays.asList(writer));
    }

    public void add(TokenCache... cache) {
        caches.addAll(Arrays.asList(cache));
    }

}
