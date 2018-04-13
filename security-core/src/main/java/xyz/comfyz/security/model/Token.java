package xyz.comfyz.security.model;

import xyz.comfyz.security.cache.Cacheable;

/**
 * @author : comfy create at 2018-04-13 10:03
 */
public class Token implements Cacheable {

    private final long createTime;
    private final long expiry;

    public Token(long createTime, long expiry) {
        this.createTime = createTime;
        this.expiry = expiry;
    }

    @Override
    public long getExpires() {
        return expiry;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

}
