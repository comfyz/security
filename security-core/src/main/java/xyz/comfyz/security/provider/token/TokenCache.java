package xyz.comfyz.security.provider.token;

/**
 * @author : comfy create at 2018-04-03 19:31
 */
public interface TokenCache {

    boolean exist(String token);

    void write(String token);

    void remove(String token);

}
