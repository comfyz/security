package xyz.comfyz.security.core.provider.token.impl;

import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.provider.token.AbstractTokenWriter;
import xyz.comfyz.security.core.SecurityContext;

import javax.servlet.http.Cookie;

/**
 * @author : comfy create at 2018-04-08 12:58
 */
public final class CookieTokenWriter extends AbstractTokenWriter {

    private final String domain;
    private final String path;

    public CookieTokenWriter(String name, int expiry, String domain, String path) {
        super(name, expiry);
        this.domain = domain;
        this.path = path;
    }

    @Override
    public void write(String token) {
        setCookie(token, expiry);
    }

    @Override
    public void remove(String token) {
        setCookie(token, 0);
    }

    private void setCookie(String value, int expiry) {
        final Cookie cookie = new Cookie(name, value);
        if (StringUtils.hasText(domain))
            cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        SecurityContext.response().addCookie(cookie);
    }

}
