package xyz.comfyz.security.model;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        11:16 2018/3/22
 * Version:     1.0
 * Description:
 */
public class AuthenticationToken<T, E extends Authority> {
    private final UserDetalis userDetails;
    private final T principal;
    private final Set<E> authorities;

    public AuthenticationToken(UserDetalis userDetails, Set<E> authorities, T principal) {
        if (userDetails == null)
            throw new IllegalArgumentException("'userDetails' is not present");

        this.userDetails = userDetails;
        this.principal = principal;

        if (authorities == null) {
            this.authorities = Collections.emptySet();
        } else {
            final Iterator<E> var2 = authorities.iterator();

            E a;
            do {
                if (!var2.hasNext()) {
                    Set<E> temp = new HashSet<>(authorities.size());
                    temp.addAll(authorities);
                    this.authorities = Collections.unmodifiableSet(temp);
                    return;
                }

                a = var2.next();
            } while (a != null);

            throw new IllegalArgumentException("Authorities collection cannot contain any null elements");
        }
    }

    public Set<E> getAuthorities() {
        return authorities;
    }

    public UserDetalis getUserDetails() {
        return userDetails;
    }

    public T getPrincipal() {
        return principal;
    }
}
