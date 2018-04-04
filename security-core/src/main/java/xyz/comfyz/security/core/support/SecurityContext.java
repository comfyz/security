package xyz.comfyz.security.core.support;


import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.provider.AuthenticationProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        14:27 2018/3/28
 * Version:     1.0
 * Description:
 */
public class SecurityContext {

    private static ThreadLocal<AuthenticationToken> authenticationTokenHolder = new ThreadLocal<>();
    private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    AuthenticationProvider authenticationTokenProvider;

    public static AuthenticationToken authenticationToken() {
        return authenticationTokenHolder.get();
    }

    public static HttpServletRequest request() {
        return requestHolder.get();
    }

    public static HttpServletResponse response() {
        return responseHolder.get();
    }

    public static void set(AuthenticationToken token) {
        authenticationTokenHolder.set(token);
    }

    public static void set(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static void set(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static void clear() {
        authenticationTokenHolder.remove();
        requestHolder.remove();
        responseHolder.remove();
    }

    public static void cacheRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        set(request);
        set(response);
    }
}
