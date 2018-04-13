package xyz.comfyz.security.cache;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存管理类
 *
 * @author jinyu(foxinmy @ gmail.com)
 * @className CacheManager
 * @date 2016年5月27日
 * @see
 * @since JDK 1.7
 */
public class CacheManager<T extends Cacheable> extends AbstractCacheManager<T> {
    private final ReentrantLock lock = new ReentrantLock();

    public CacheManager(CacheCreator<T> cacheCreator, CacheStorager<T> cacheStorager) {
        super(cacheCreator, cacheStorager);
    }

    /**
     * 获取缓存对象
     *
     * @return 缓存对象
     */
    public T getCache() {
        String cacheKey = cacheCreator.key();
        T cache = cacheStorager.lookup(cacheKey);
        try {
            if (cache == null && lock.tryLock(3, TimeUnit.SECONDS)) {
                try {
                    cache = cacheStorager.lookup(cacheKey);
                    if (cache == null) {
                        cache = cacheCreator.create();
                        cacheStorager.caching(cacheKey, cache);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
        }
        return cache;
    }

    /**
     * 刷新缓存对象
     *
     * @return 缓存对象
     */
    public T refreshCache() {
        String cacheKey = cacheCreator.key();
        T cache = cacheCreator.create();
        cacheStorager.caching(cacheKey, cache);
        return cache;
    }

}
