package xyz.comfyz.security.model;

import xyz.comfyz.security.support.AntPathRequestMatcher;

/**
 * @author : comfy create at 2018-04-09 15:12
 */
public class Authority {
    private final AntPathRequestMatcher requestMatcher;

    public Authority(AntPathRequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public AntPathRequestMatcher getRequestMatcher() {
        return requestMatcher;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Authority))
            return false;

        if (obj == this)
            return true;

        return this.requestMatcher.equals(((Authority) obj).requestMatcher);
    }
}
