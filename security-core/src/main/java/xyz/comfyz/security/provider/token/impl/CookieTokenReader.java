package xyz.comfyz.security.provider.token.impl;

import xyz.comfyz.security.support.SecurityContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : comfy create at 2018-04-04 15:45
 */
public final class CookieTokenReader {

    public String read(String key) {
        final AtomicReference<String> secret = new AtomicReference<>();
        final Cookie[] cookies = SecurityContext.request().getCookies();
        if (cookies != null)
            Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(key))
                    .findFirst().map(Cookie::getValue).ifPresent(secret::set);
        return secret.get();
    }

}
