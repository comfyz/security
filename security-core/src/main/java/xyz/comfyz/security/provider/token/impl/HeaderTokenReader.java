package xyz.comfyz.security.provider.token.impl;

import xyz.comfyz.security.SecurityContext;
import xyz.comfyz.security.provider.token.AbstractTokenReader;

/**
 * @author : comfy create at 2018-04-04 15:55
 */
public final class HeaderTokenReader extends AbstractTokenReader {

    public HeaderTokenReader(String name, int expiry) {
        super(name, expiry);
    }

    @Override
    public String read() {
        return SecurityContext.request().getHeader(this.name);
    }

    @Override
    public int sort() {
        return 50;
    }
}
