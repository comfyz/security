package xyz.comfyz.security.core.model;


import xyz.comfyz.security.core.util.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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
    private final Collection<AntPathRequestMatcher> authorities;

    public AuthenticationToken(UserDetalis userDetails, Collection<AntPathRequestMatcher> authorities, Object principal) {
        if (userDetails == null
                || principal == null)
            throw new IllegalArgumentException("'userDetails' and 'principal' is not present");

        this.userDetails = userDetails;
        this.principal = principal;

        if (authorities == null) {
            this.authorities = Collections.emptyList();
        } else {
            final Iterator var2 = authorities.iterator();

            AntPathRequestMatcher a;
            do {
                if (!var2.hasNext()) {
                    ArrayList<AntPathRequestMatcher> temp = new ArrayList<>(authorities.size());
                    temp.addAll(authorities);
                    this.authorities = Collections.unmodifiableList(temp);
                    return;
                }

                a = (AntPathRequestMatcher) var2.next();
            } while (a != null);

            throw new IllegalArgumentException("Authorities collection cannot contain any null elements");
        }
    }

    public Collection<AntPathRequestMatcher> getAuthorities() {
        return authorities;
    }

    public UserDetalis getUserDetails() {
        return userDetails;
    }

    public Object getPrincipal() {
        return principal;
    }
}
