package xyz.comfyz.security.cache;

/**
 * @author : comfy create at 2018-04-13 11:13
 */
public abstract class AbstractCacheManager<T extends Cacheable> {
    protected final CacheCreator<T> cacheCreator;
    protected final CacheStorager<T> cacheStorager;

    public AbstractCacheManager(CacheCreator<T> cacheCreator, CacheStorager<T> cacheStorager) {
        this.cacheCreator = cacheCreator;
        this.cacheStorager = cacheStorager;
    }

    /**
     * 获取缓存对象
     *
     * @return 缓存对象
     */
    public abstract T getCache();

    /**
     * 刷新缓存对象
     *
     * @return 缓存对象
     */
    public abstract T refreshCache();

    /**
     * 移除缓存
     *
     * @return 被移除的缓存对象
     */
    public T evictCache() {
        String cacheKey = cacheCreator.key();
        return cacheStorager.evict(cacheKey);
    }

    /**
     * 清除所有的缓存(<font color="red">请慎重</font>)
     */
    public void clearCache() {
        cacheStorager.clear();
    }
}
