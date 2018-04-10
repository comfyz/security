package xyz.comfyz.security;


import xyz.comfyz.security.model.AuthenticationToken;

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

    private final static ThreadLocal<String> token = new ThreadLocal<>();
    private final static ThreadLocal<AuthenticationToken> authenticationToken = new ThreadLocal<>();
    private final static ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private final static ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public static String token() {
        return token.get();
    }

    public static AuthenticationToken authenticationToken() {
        return authenticationToken.get();
    }

    public static HttpServletRequest request() {
        return request.get();
    }

    public static HttpServletResponse response() {
        return response.get();
    }

    public static void set(String token) {
        SecurityContext.token.set(token);
    }

    public static void set(AuthenticationToken token) {
        authenticationToken.set(token);
    }

    public static void cacheRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext.request.set(request);
        SecurityContext.response.set(response);
    }

    public static void clear() {
        token.remove();
        authenticationToken.remove();
        request.remove();
        response.remove();
    }
}
