package xyz.comfyz.security.provider.auth;


import xyz.comfyz.security.model.AuthenticationToken;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public interface AuthenticationTokenCache {

    AuthenticationToken get(String userId);

    void cache(AuthenticationToken token);

    void clear();

    void remove(String userId);
}
