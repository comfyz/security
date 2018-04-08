package xyz.comfyz.security.core.provider.token.impl;

import xyz.comfyz.security.core.provider.token.AbstractTokenReader;
import xyz.comfyz.security.core.SecurityContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : comfy create at 2018-04-04 15:45
 */
public final class CookieTokenReader extends AbstractTokenReader {

    private final String domain;
    private final String path;

    public CookieTokenReader(String name, int expiry, String domain, String path) {
        super(name, expiry);
        this.domain = domain;
        this.path = path;
    }

    @Override
    public String read() {
        final AtomicReference<String> secret = new AtomicReference<>();
        final Cookie[] cookies = SecurityContext.request().getCookies();
        if (cookies != null)
            Arrays.stream
                    (SecurityContext.request().getCookies())
                    .filter(cookie -> this.name.equals(cookie.getName()))
                    .findFirst().map(Cookie::getValue).ifPresent(secret::set);
        return secret.get();
    }

    @Override
    public int sort() {
        return 70;
    }
}
