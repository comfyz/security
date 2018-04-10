package xyz.comfyz.security.provider.token.impl;

import org.springframework.util.StringUtils;
import xyz.comfyz.security.provider.token.AbstractTokenCache;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : comfy create at 2018-04-04 11:23
 */
public final class HeapTokenCache extends AbstractTokenCache {

    private final ConcurrentHashMap<String, Life> cache = new ConcurrentHashMap<>();

    public HeapTokenCache(String name, int expiry) {
        super(name, expiry);
    }

    @Override
    public boolean exist(String token) {
        if (StringUtils.hasText(token)
                && cache.get(token) != null)
            return expiry < 0 || cache.get(token).getBirth().plusSeconds(cache.get(token).getAge()).isAfter(Instant.now());
        return false;
    }

    @Override
    public void write(String token) {
        if (!StringUtils.hasText(token))
            throw new IllegalArgumentException("'token' must be present and not be empty.");
        if (expiry < 0 && cache.get(token) != null)
            return;
        final int old = cache.get(token) == null ? 0 : cache.get(token).getAge();
        cache.put(token, new Life().setAge(old + expiry));
    }

    @Override
    public void remove(String token) {
        cache.remove(token);
    }

    private class Life {
        private final Instant birth;
        private int age;

        private Life() {
            this.birth = Instant.now();
        }

        Instant getBirth() {
            return birth;
        }

        int getAge() {
            return age;
        }

        Life setAge(int age) {
            this.age = age;
            return this;
        }
    }
}
