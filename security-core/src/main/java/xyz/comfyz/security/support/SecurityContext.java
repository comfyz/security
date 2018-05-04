package xyz.comfyz.security.support;


import xyz.comfyz.security.model.Authentication;

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
    private final static ThreadLocal<Authentication> authentication = new ThreadLocal<>();
    private final static ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    private final static ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();

    public static String userId() {
        if (authentication.get() != null && authentication.get().getUserDetails() != null)
            return authentication.get().getUserDetails().getUserId();
        return null;
    }

    public static String token() {
        return token.get();
    }

    public static Authentication authentication() {
        return authentication.get();
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

    public static void set(Authentication token) {
        authentication.set(token);
    }

    public static void cacheRequestResponse(HttpServletRequest request, HttpServletResponse response) {
        SecurityContext.request.set(request);
        SecurityContext.response.set(response);
    }

    public static void clear() {
        token.remove();
        authentication.remove();
        request.remove();
        response.remove();
    }
}
