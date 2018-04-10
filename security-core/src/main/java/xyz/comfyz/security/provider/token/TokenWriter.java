package xyz.comfyz.security.provider.token;

/**
 * @author : comfy create at 2018-04-08 13:05
 */
public interface TokenWriter extends Comparable<TokenWriter> {

    default int sort() {
        return 0;
    }

    public void write(String token);

    public void remove(String token);

}
