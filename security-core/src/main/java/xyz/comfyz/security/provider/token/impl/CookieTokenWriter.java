package xyz.comfyz.security.provider.token.impl;

import org.springframework.util.StringUtils;
import xyz.comfyz.security.support.SecurityContext;
import xyz.comfyz.security.provider.token.AbstractTokenWriter;

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
        if (StringUtils.hasText(domain)) {
            addLocalCookie(value, expiry);
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        SecurityContext.response().addCookie(cookie);
    }

    private void addLocalCookie(String value, int expiry) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        SecurityContext.response().addCookie(cookie);
    }

}
