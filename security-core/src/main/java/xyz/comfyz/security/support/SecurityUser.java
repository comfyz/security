package xyz.comfyz.security.support;

import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.model.UserDetalis;

import java.util.Collections;

/**
 * @author : comfy create at 2018-04-12 16:13
 */
public enum SecurityUser {
    ANONYMOUS(new Authentication<>(new UserDetalis(null, "ANONYMOUS", false), Collections.emptySet(), null));

    private final Authentication token;

    private SecurityUser(Authentication token) {
        this.token = token;
    }

    public Authentication authentication() {
        return this.token;
    }

}
