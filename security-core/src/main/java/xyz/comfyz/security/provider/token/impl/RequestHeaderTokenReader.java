package xyz.comfyz.security.provider.token.impl;

import xyz.comfyz.security.provider.token.AbstractTokenReader;
import xyz.comfyz.security.support.SecurityContext;

/**
 * @author : comfy create at 2018-04-04 15:55
 */
public final class RequestHeaderTokenReader extends AbstractTokenReader {

    @Override
    public String read(String key) {
        return SecurityContext.request().getHeader(key);
    }

    @Override
    public int sort() {
        return 50;
    }
}
