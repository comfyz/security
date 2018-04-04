package xyz.comfyz.security.core.provider.token.impl;

import xyz.comfyz.security.core.provider.token.AbstractTokenReader;
import xyz.comfyz.security.core.support.HttpSecurity;
import xyz.comfyz.security.core.support.SecurityContext;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : comfy create at 2018-04-04 15:45
 */
public class CookieTokenReader extends AbstractTokenReader {

    @Override
    public String read() {
        AtomicReference<String> secret = new AtomicReference<>();
        Arrays.stream(SecurityContext.request().getCookies())
                .filter(cookie -> HttpSecurity.getCookieConfig().getName().equals(cookie.getName()))
                .findFirst().map(Cookie::getValue).ifPresent(secret::set);
        return secret.get();
    }

    @Override
    public int sort() {
        return 70;
    }
}
