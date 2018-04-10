package xyz.comfyz.security.provider.token;

import org.springframework.util.StringUtils;

/**
 * @author : comfy create at 2018-04-04 15:48
 */
public abstract class AbstractTokenReader implements TokenReader {

    protected final String name;
    protected final int expiry;

    public AbstractTokenReader(String name, int expiry) {
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
    public int compareTo(TokenReader o) {
        return Integer.compare(this.sort(), o.sort());
    }
}
