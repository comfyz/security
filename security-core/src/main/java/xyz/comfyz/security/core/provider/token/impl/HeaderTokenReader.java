package xyz.comfyz.security.core.provider.token.impl;

import xyz.comfyz.security.core.provider.token.AbstractTokenReader;
import xyz.comfyz.security.core.support.HttpSecurity;
import xyz.comfyz.security.core.support.SecurityContext;

/**
 * @author : comfy create at 2018-04-04 15:55
 */
public class HeaderTokenReader extends AbstractTokenReader {

    @Override
    public String read() {
        return SecurityContext.request().getHeader(HttpSecurity.getTokenName());
    }

    @Override
    public int sort() {
        return 50;
    }
}
