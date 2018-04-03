package xyz.comfyz.security.core.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.core.access.SecurityContext;
import xyz.comfyz.security.core.access.common.HttpSecurity;
import xyz.comfyz.security.core.access.common.SecurityMetadataSource;
import xyz.comfyz.security.core.model.AuthenticationToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;


public class SecurityUtils {
    public static String signIn(AuthenticationToken token) {
        if (token != null && token.getUserDetails() != null && StringUtils.hasText(token.getUserDetails().getUserId())) {
            String secret = encrypt(token);
            Cookie cky = createCookie(secret);
            Cookie ckyLocal = createCookieLocal(secret);
            ckyLocal.setValue(secret);
            cky.setValue(secret);
            SecurityContext.getHttpServletResponse().addCookie(cky);
            SecurityContext.getHttpServletResponse().addCookie(ckyLocal);
            addSession(token);
            return secret;
        }
        return "";
    }

    public static void signOut() {
        removeCookie();
        removeSession();
    }

    public static void removeCookie() {
        Cookie cookie = createCookie("");
        Cookie ckyLocal = createCookieLocal("");
        cookie.setMaxAge(0);
        ckyLocal.setMaxAge(0);
        SecurityContext.getHttpServletResponse().addCookie(cookie);
        SecurityContext.getHttpServletResponse().addCookie(ckyLocal);
    }

    public static void removeSession() {
        SecurityContext.getHttpServletRequest().getSession().removeAttribute("AuthenticationToken");
    }

    public static String getUserId() {
        String userId = null;
        //cookie
        Cookie[] cookies = SecurityContext.getHttpServletRequest().getCookies();
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                if (cooky.getName().equals(HttpSecurity.getCookieConfig().getName()) && StringUtils.hasText(cooky.getValue())) {
                    userId = decode(cooky.getValue(), "userId");
                }
            }
        }

        if (!StringUtils.hasText(userId)) {
            //header
            String tokenStr = SecurityContext.getHttpServletRequest().getHeader(HttpSecurity.getCookieConfig().getName());
            if (StringUtils.hasText(tokenStr)) {
                userId = decode(tokenStr, "userId");
            }
        }

        return userId;
    }

    private static void addSession(AuthenticationToken token) {
        HttpSession session = SecurityContext.getHttpServletRequest().getSession();
        session.setMaxInactiveInterval(HttpSecurity.getCookieConfig().getExpiry());
        session.setAttribute("AuthenticationToken", token);
    }

    public static AuthenticationToken getSessionToken() {
        AuthenticationToken token = null;
        //session
        Object obj = SecurityContext.getHttpServletRequest().getSession().getAttribute("AuthenticationToken");
        if (obj != null) {
            token = (AuthenticationToken) obj;
        }
        return token;
    }

    public static boolean isLogin() {
        return SecurityContext.getAuthenticationToken() != null;
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

    public static Cookie createCookie(String value) {
        Cookie cky = new Cookie(HttpSecurity.getCookieConfig().getName(), value);
        cky.setDomain(HttpSecurity.getCookieConfig().getDomain());
        cky.setPath(HttpSecurity.getCookieConfig().getPath());
        cky.setMaxAge(HttpSecurity.getCookieConfig().getExpiry());
        return cky;
    }

    public static Cookie createCookieLocal(String value) {
        Cookie cky = new Cookie(HttpSecurity.getCookieConfig().getName(), value);
        cky.setPath(HttpSecurity.getCookieConfig().getPath());
        cky.setMaxAge(HttpSecurity.getCookieConfig().getExpiry());
        return cky;
    }

    public static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    private static String decode(String source, String key) {
        String result = null;
        try {
            String code = EncryptUtil.aesDecrypt(source);
            JSONObject obj = (JSONObject) JSONObject.parse(code);
            if (obj != null) {
                result = obj.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean match(String url, String method) {
        return SecurityMetadataSource.match(url, method);
    }
}
