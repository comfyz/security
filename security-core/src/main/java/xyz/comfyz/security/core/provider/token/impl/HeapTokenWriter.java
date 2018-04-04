package xyz.comfyz.security.core.provider.token.impl;

import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.provider.token.TokenWriter;
import xyz.comfyz.security.core.support.HttpSecurity;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : comfy create at 2018-04-04 11:23
 */
public final class HeapTokenWriter implements TokenWriter {

    private final ConcurrentHashMap<String, Instant> cache = new ConcurrentHashMap<>();

    @Override
    public boolean exist(String token) {
        return StringUtils.hasText(token)
                && cache.get(token) != null
                && cache.get(token).plusSeconds(HttpSecurity.getCookieConfig().getExpiry()).isAfter(Instant.now());
    }

    @Override
    public void write(String token) {
        cache.put(token, Instant.now().plusSeconds(HttpSecurity.getCookieConfig().getExpiry()));
    }

    @Override
    public void remove(String token) {
        cache.remove(token);
    }
}
