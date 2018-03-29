package xyz.comfyz.security.core.access.common;

import xyz.comfyz.security.core.model.AuthenticationToken;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:21 2018/3/26
 * Version:     1.0
 * Description:
 */
public interface UserDetailsService {
    AuthenticationToken loadUser(String userId);
}
