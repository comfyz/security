package xyz.comfyz.security.provider;


import xyz.comfyz.security.model.Authentication;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:21 2018/3/26
 * Version:     1.0
 * Description:
 */
public interface UserDetailsService {
    Authentication loadUser(String userId);
}
