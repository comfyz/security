package xyz.comfyz.security.support;

import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.model.UserDetalis;

import java.util.Collections;

/**
 * @author : comfy create at 2018-04-12 16:13
 */
public enum SecurityUser {
    ANONYMOUS(new AuthenticationToken<>(new UserDetalis(null, "ANONYMOUS", false), Collections.emptySet(), null));

    private final AuthenticationToken token;

    private SecurityUser(AuthenticationToken token) {
        this.token = token;
    }

    public AuthenticationToken authenticationToken() {
        return this.token;
    }

}
