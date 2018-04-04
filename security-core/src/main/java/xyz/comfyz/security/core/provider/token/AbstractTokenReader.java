package xyz.comfyz.security.core.provider.token;

/**
 * @author : comfy create at 2018-04-04 15:48
 */
public abstract class AbstractTokenReader implements TokenReader {
    @Override
    public int sort() {
        return 100;
    }

    @Override
    public int compareTo(TokenReader o) {
        return Integer.compare(this.sort(), o.sort());
    }
}
