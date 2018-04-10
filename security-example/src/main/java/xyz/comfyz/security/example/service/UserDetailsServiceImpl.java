package xyz.comfyz.security.example.service;

import org.springframework.stereotype.Service;
import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.model.Authority;
import xyz.comfyz.security.model.UserDetalis;
import xyz.comfyz.security.provider.UserDetailsService;
import xyz.comfyz.security.util.AntPathRequestMatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:50 2018/3/26
 * Version:     1.0
 * Description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static Map<String, AuthenticationToken> users;

    static {
        users = new HashMap<>();
        users.put("10001", new AuthenticationToken(new UserDetalis("10001", "admin", true)
                , Collections.emptySet(), new Object()));
        users.put("10002", new AuthenticationToken(new UserDetalis("10002", "zhangsan", false)
                , Collections.singleton(new Authority(new AntPathRequestMatcher("/role/zhangsan/**"))), new Object()));
        users.put("10003", new AuthenticationToken(new UserDetalis("10003", "lisi", false)
                , Collections.singleton(new Authority(new AntPathRequestMatcher("/role/lisi/**"))), new Object()));

    }

    @Override
    public AuthenticationToken loadUser(String userId) {
        return users.get(userId);
    }

}
