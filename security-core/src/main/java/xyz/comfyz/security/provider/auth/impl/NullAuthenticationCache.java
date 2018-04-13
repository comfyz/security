package xyz.comfyz.security.provider.auth.impl;


import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.provider.auth.AuthenticationCache;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public final class NullAuthenticationCache implements AuthenticationCache {

    @Override
    public Authentication get(String userId) {
        return null;
    }

    @Override
    public void cache(Authentication token) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(String userId) {

    }
}
