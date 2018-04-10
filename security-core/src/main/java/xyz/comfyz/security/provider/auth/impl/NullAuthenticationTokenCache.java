package xyz.comfyz.security.provider.auth.impl;


import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.provider.auth.AuthenticationTokenCache;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public final class NullAuthenticationTokenCache implements AuthenticationTokenCache {

    @Override
    public AuthenticationToken get(String userId) {
        return null;
    }

    @Override
    public void cache(AuthenticationToken token) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(String userId) {

    }
}
