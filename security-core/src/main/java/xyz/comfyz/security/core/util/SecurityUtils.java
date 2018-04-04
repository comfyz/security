package xyz.comfyz.security.core.util;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.provider.TokenProvider;
import xyz.comfyz.security.core.support.SecurityContext;

/**
 * Created by sage on 2017/11/19.
 */
@Component
public class SecurityUtils {

    public static String signIn(AuthenticationToken token) {
        if(token == null)
            throw new NullPointerException("token ");
        String secret = SecretUtils.encrypt(token.getUserDetails());
        TokenProvider.flush(secret);
        return secret;
    }

    public static void signOut() {
    }

    public static String getRequestPath() {
        String url = SecurityContext.request().getServletPath();
        if (SecurityContext.request().getPathInfo() != null) {
            url = url + SecurityContext.request().getPathInfo();
        }
        return url;
    }

    public static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

}
