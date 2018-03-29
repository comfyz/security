package xyz.comfyz.security.core.cache.impl;


import xyz.comfyz.security.core.cache.UserDetailsCache;
import xyz.comfyz.security.core.model.AuthenticationToken;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public class NullUserDetailsCache implements UserDetailsCache {
    @Override
    public AuthenticationToken get(String userId) {
        return null;
    }

    @Override
    public void put(AuthenticationToken userDetalis) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(String userId) {

    }
}
