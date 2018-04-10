package xyz.comfyz.security.example.service;

import org.springframework.stereotype.Service;
import xyz.comfyz.security.example.bo.Auth;
import xyz.comfyz.security.example.bo.User;
import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.model.UserDetalis;
import xyz.comfyz.security.provider.UserDetailsService;
import xyz.comfyz.security.support.AntPathRequestMatcher;

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

    private static Map<String, AuthenticationToken<User, Auth>> users;

    static {
        users = new HashMap<>();
        users.put("10001", new AuthenticationToken<>(new UserDetalis("10001", "admin", true)
                , Collections.emptySet(), new User()));
        users.put("10002", new AuthenticationToken<>(new UserDetalis("10002", "zhangsan", false)
                , Collections.singleton(new Auth(new AntPathRequestMatcher("/role/zhangsan/**"), 2)), new User()));
        users.put("10003", new AuthenticationToken<>(new UserDetalis("10003", "lisi", false)
                , Collections.singleton(new Auth(new AntPathRequestMatcher("/role/lisi/**"), 3)), new User()));

    }

    @Override
    public AuthenticationToken<User, Auth> loadUser(String userId) {
        return users.get(userId);
    }

}
