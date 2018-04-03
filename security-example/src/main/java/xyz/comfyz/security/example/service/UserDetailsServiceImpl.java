package xyz.comfyz.security.example.service;

import org.springframework.stereotype.Service;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.model.UserDetalis;
import xyz.comfyz.security.core.access.common.UserDetailsService;
import xyz.comfyz.security.core.util.AntPathRequestMatcher;

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
                , Collections.emptyList()));
        users.put("10002", new AuthenticationToken(new UserDetalis("10002", "zhangsan", false)
                , Collections.singletonList(new AntPathRequestMatcher("/role/zhangsan/**"))));
        users.put("10003", new AuthenticationToken(new UserDetalis("10003", "lisi", false)
                , Collections.singletonList(new AntPathRequestMatcher("/role/lisi/**"))));

    }

    @Override
    public AuthenticationToken loadUser(String userId) {
        return users.get(userId);
    }

}
