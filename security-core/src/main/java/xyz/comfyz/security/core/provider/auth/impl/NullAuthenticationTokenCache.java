package xyz.comfyz.security.core.provider.auth.impl;


import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.provider.auth.AuthenticationTokenCache;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public class NullAuthenticationTokenCache implements AuthenticationTokenCache {

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
