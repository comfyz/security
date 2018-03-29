package xyz.comfyz.security.core.common;


import xyz.comfyz.security.core.model.AuthenticationToken;

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

    public static AuthenticationToken getAuthenticationToken() {
        return authenticationTokenHolder.get();
    }

    public static void set(AuthenticationToken token) {
        authenticationTokenHolder.set(token);
    }

    private static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static HttpServletRequest getHttpServletRequest() {
        return requestHolder.get();
    }

    public static void set(HttpServletRequest request) {
        requestHolder.set(request);
    }

    private static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    public static HttpServletResponse getHttpServletResponse() {
        return responseHolder.get();
    }

    public static void set(HttpServletResponse response) {
        responseHolder.set(response);
    }
}
