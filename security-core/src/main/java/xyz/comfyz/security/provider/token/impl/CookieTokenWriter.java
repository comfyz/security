package xyz.comfyz.security.provider.token.impl;

import org.springframework.util.StringUtils;
import xyz.comfyz.security.support.SecurityContext;

import javax.servlet.http.Cookie;

/**
 * @author : comfy create at 2018-04-08 12:58
 */
public final class CookieTokenWriter {

    public void write(String domain, int expiry, String path, String name, String value, boolean debug) {
        setCookie(domain, expiry, path, name, value, debug);
    }

    public void remove(String domain, String path, String name, String value) {
        setCookie(domain, 0, path, name, value, true);
    }

    private void setCookie(String domain, int expiry, String path, String name, String value, boolean debug) {
        final Cookie cookie = new Cookie(name, value);
        if (StringUtils.hasText(domain)) {
            if (debug)
                addLocalCookie(path, name, value, expiry);
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        SecurityContext.response().addCookie(cookie);
    }

    private void addLocalCookie(String path, String name, String value, int expiry) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(expiry);
        SecurityContext.response().addCookie(cookie);
    }

}
