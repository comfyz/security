package xyz.comfyz.security.core.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.access.common.HttpSecurity;
import xyz.comfyz.security.core.access.SecurityContext;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.access.common.SecurityMetadataSource;

import javax.servlet.http.Cookie;

/**
 * Created by sage on 2017/11/19.
 */
public class SecurityUtils {
    public static String signIn(AuthenticationToken token) {
        if (token == null || token.getUserDetails() == null || !StringUtils.hasText(token.getUserDetails().getUserId()))
            throw new IllegalArgumentException("用户实体为空");
        String secret = encrypt(token);
        Cookie cky = createCookie();
        Cookie ckyLocal = createCookieLocal();
        ckyLocal.setValue(secret);
        cky.setValue(secret);
        SecurityContext.getHttpServletResponse().addCookie(cky);
        SecurityContext.getHttpServletResponse().addCookie(ckyLocal);
        addSession(token);
        return secret;
    }

    public static void signOut() {
        removeCookie();
        removeSession();
    }

    public static void removeCookie() {
        Cookie cookie = createCookie();
        Cookie ckyLocal = createCookieLocal();
        cookie.setMaxAge(0);
        ckyLocal.setMaxAge(0);
        SecurityContext.getHttpServletResponse().addCookie(cookie);
        SecurityContext.getHttpServletResponse().addCookie(ckyLocal);
    }

    public static void removeSession() {
        SecurityContext.getHttpServletRequest().getSession().removeAttribute("AuthenticationToken");
    }

    public static String getUserId() {
        Cookie[] cookies = SecurityContext.getHttpServletRequest().getCookies();
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals(HttpSecurity.getCookieConfig().getName()) && StringUtils.hasText(cooky.getValue())) {
                    String code = EncryptUtil.aesDecrypt(cooky.getValue());
                    JSONObject obj = (JSONObject) JSONObject.parse(code);
                    if (obj != null) {
                        return obj.getString("userId");
                    }
                }
            }
        }
        return null;
        //返回默认用户
        //return "0087e6bfbc79458f945da8be3a563807";
    }

    private static void addSession(AuthenticationToken token) {
        SecurityContext.getHttpServletRequest().getSession().setAttribute("AuthenticationToken", token);
    }

    public static AuthenticationToken getSessionToken() {
        AuthenticationToken token = null;
        String userId = getUserId();
        if (StringUtils.hasText(userId)) {
            //session
            Object obj = SecurityContext.getHttpServletRequest().getSession().getAttribute("AuthenticationToken");
            if (obj != null) {
                token = (AuthenticationToken) obj;
            }
        }

        return token;
    }

    public static boolean isLogin() {
        return StringUtils.hasText(getUserId());
    }

    public static String getRequestPath() {
        String url = SecurityContext.getHttpServletRequest().getServletPath();
        if (SecurityContext.getHttpServletRequest().getPathInfo() != null) {
            url = url + SecurityContext.getHttpServletRequest().getPathInfo();
        }
        return url;
    }

    public static String encrypt(AuthenticationToken token) {
        JSONObject body = new JSONObject();
        body.put("userId", token.getUserDetails().getUserId());
        body.put("userName", token.getUserDetails().getUserName());
        return EncryptUtil.aesEncrypt(body.toJSONString());
    }

    public static Cookie createCookie() {
        Cookie cky = new Cookie(HttpSecurity.getCookieConfig().getName(), "");
        cky.setDomain(HttpSecurity.getCookieConfig().getDomain());
        cky.setPath(HttpSecurity.getCookieConfig().getPath());
        cky.setMaxAge(HttpSecurity.getCookieConfig().getExpiry());
        return cky;
    }

    public static Cookie createCookieLocal() {
        Cookie cky = new Cookie(HttpSecurity.getCookieConfig().getName(), "");
        cky.setPath(HttpSecurity.getCookieConfig().getPath());
        cky.setMaxAge(HttpSecurity.getCookieConfig().getExpiry());
        return cky;
    }

    public boolean match(String url, String method) {
        return SecurityMetadataSource.match(url, method);
    }

    public static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }
}
