package xyz.comfyz.security.provider.token;

/**
 * @author : comfy create at 2018-04-04 15:32
 */
public interface TokenReader extends Comparable<TokenReader> {

    default int sort() {
        return 0;
    }

    public String read(String key);
}
