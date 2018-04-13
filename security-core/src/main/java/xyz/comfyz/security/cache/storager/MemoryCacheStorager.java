package xyz.comfyz.security.cache.storager;

import xyz.comfyz.security.cache.CacheStorager;
import xyz.comfyz.security.cache.Cacheable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用内存保存缓存对象(不推荐使用)
 *
 * @author jinyu(foxinmy @ gmail.com)
 * @className MemoryCacheStorager
 * @date 2016年1月24日
 * @see
 * @since JDK 1.6
 */
public class MemoryCacheStorager<T extends Cacheable> implements
        CacheStorager<T> {

    private final Map<String, T> CONMAP;

    public MemoryCacheStorager() {
        this.CONMAP = new ConcurrentHashMap<String, T>();
    }

    @Override
    public T lookup(String key) {
        T cache = this.CONMAP.get(key);
        if (cache != null) {
            if ((cache.getCreateTime() + cache.getExpires() - CUTMS) > System
                    .currentTimeMillis()) {
                return cache;
            }
        }
        return null;
    }

    @Override
    public void caching(String key, T cache) {
        this.CONMAP.put(key, cache);
    }

    @Override
    public T evict(String key) {
        return this.CONMAP.remove(key);
    }

    @Override
    public void clear() {
        this.CONMAP.clear();
    }
}
