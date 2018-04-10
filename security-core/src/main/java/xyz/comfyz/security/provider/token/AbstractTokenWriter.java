package xyz.comfyz.security.provider.token;

import org.springframework.util.StringUtils;

/**
 * @author : comfy create at 2018-04-08 13:07
 */
public abstract class AbstractTokenWriter implements TokenWriter {

    protected final String name;
    protected final int expiry;

    public AbstractTokenWriter(String name, int expiry) {
        if (!StringUtils.hasText(name))
            throw new IllegalArgumentException("'tokenName' is not present.");
        if (expiry == 0)
            throw new IllegalArgumentException("'expiry' must not be 0.");
        this.name = name;
        this.expiry = expiry;
    }

    @Override
    public int sort() {
        return 100;
    }

    @Override
    public int compareTo(TokenWriter o) {
        return Integer.compare(this.sort(), o.sort());
    }
}
