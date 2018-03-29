package xyz.comfyz.security.core.cache;


import xyz.comfyz.security.core.model.AuthenticationToken;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        10:13 2018/3/28
 * Version:     1.0
 * Description:
 */
public interface UserDetailsCache {

    AuthenticationToken get(String userId);

    void put(AuthenticationToken userDetalis);

    void clear();

    void remove(String userId);
}
