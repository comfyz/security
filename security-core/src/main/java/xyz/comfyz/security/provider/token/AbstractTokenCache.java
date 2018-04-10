package xyz.comfyz.security.provider.token;

import org.springframework.util.StringUtils;

/**
 * @author : comfy create at 2018-04-08 11:25
 */
public abstract class AbstractTokenCache implements TokenCache {
    protected final String name;
    protected final int expiry;

    public AbstractTokenCache(String name, int expiry) {
        if (!StringUtils.hasText(name))
            throw new IllegalArgumentException("'tokenName' is not present.");
        if (expiry == 0)
            throw new IllegalArgumentException("'expiry' must not be 0.");
        this.name = name;
        this.expiry = expiry;
    }
}
