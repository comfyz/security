package xyz.comfyz.security.model;


import xyz.comfyz.security.util.AntPathRequestMatcher;

import java.util.*;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        11:16 2018/3/22
 * Version:     1.0
 * Description:
 */
public class AuthenticationToken {
    private final UserDetalis userDetails;
    private final Object principal;
    private final Set<Authority> authorities;

    public AuthenticationToken(UserDetalis userDetails, Set<Authority> authorities, Object principal) {
        if (userDetails == null
                || principal == null)
            throw new IllegalArgumentException("'userDetails' and 'principal' is not present");

        this.userDetails = userDetails;
        this.principal = principal;

        if (authorities == null) {
            this.authorities = Collections.emptySet();
        } else {
            final Iterator var2 = authorities.iterator();

            AntPathRequestMatcher a;
            do {
                if (!var2.hasNext()) {
                    Set<Authority> temp = new HashSet<>(authorities.size());
                    temp.addAll(authorities);
                    this.authorities = Collections.unmodifiableSet(temp);
                    return;
                }

                a = (AntPathRequestMatcher) var2.next();
            } while (a != null);

            throw new IllegalArgumentException("Authorities collection cannot contain any null elements");
        }
    }

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public UserDetalis getUserDetails() {
        return userDetails;
    }

    public Object getPrincipal() {
        return principal;
    }
}
