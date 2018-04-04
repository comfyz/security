package xyz.comfyz.security.core.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

public final class AntPathRequestMatcher {
    private static final Log logger = LogFactory.getLog(AntPathRequestMatcher.class);
    private static final String MATCH_ALL = "/**";
    private final AntPathRequestMatcher.Matcher matcher;
    private final String pattern;
    private final HttpMethod httpMethod;
    private final boolean caseSensitive;

    public AntPathRequestMatcher(String pattern) {
        this(pattern, (String) null);
    }

    public AntPathRequestMatcher(String pattern, String httpMethod) {
        this(pattern, httpMethod, true);
    }

    public AntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        this.caseSensitive = caseSensitive;
        if (!pattern.equals("/**") && !pattern.equals("**")) {
            if (pattern.endsWith("/**") && pattern.indexOf(63) == -1 && pattern.indexOf(123) == -1 && pattern.indexOf(125) == -1 && pattern.indexOf("*") == pattern.length() - 2) {
                this.matcher = new AntPathRequestMatcher.SubpathMatcher(pattern.substring(0, pattern.length() - 3), caseSensitive);
            } else {
                this.matcher = new AntPathRequestMatcher.SpringAntMatcher(pattern, caseSensitive);
            }
        } else {
            pattern = "/**";
            this.matcher = null;
        }

        this.pattern = pattern;
        this.httpMethod = StringUtils.hasText(httpMethod) ? HttpMethod.valueOf(httpMethod) : null;
    }

    private static HttpMethod valueOf(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public boolean matches(HttpServletRequest request) {
        if (this.httpMethod != null && StringUtils.hasText(request.getMethod()) && this.httpMethod != valueOf(request.getMethod())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request '" + request.getMethod() + " " + this.getRequestPath(request) + "' doesn't match '" + this.httpMethod + " " + this.pattern);
            }

            return false;
        } else if (this.pattern.equals("/**")) {
            if (logger.isDebugEnabled()) {
                logger.debug("Request '" + this.getRequestPath(request) + "' matched by universal pattern '/**'");
            }

            return true;
        } else {
            String url = this.getRequestPath(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Checking match of request : '" + url + "'; against '" + this.pattern + "'");
            }

            return this.matcher.matches(url);
        }
    }

    public boolean matches(String url) {
        if (this.pattern.equals("/**")) {
            if (logger.isDebugEnabled()) {
                logger.debug("Url '" + url + "' matched by universal pattern '/**'");
            }

            return true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Checking match of url : '" + url + "'; against '" + this.pattern + "'");
            }

            return this.matcher.matches(url);
        }
    }

    public Map<String, String> extractUriTemplateVariables(HttpServletRequest request) {
        if (this.matcher != null && this.matches(request)) {
            String url = this.getRequestPath(request);
            return this.matcher.extractUriTemplateVariables(url);
        } else {
            return Collections.emptyMap();
        }
    }

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();
        if (request.getPathInfo() != null) {
            url = url + request.getPathInfo();
        }

        return url;
    }

    public String getPattern() {
        return this.pattern;
    }

    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AntPathRequestMatcher)) {
            return false;
        } else {
            AntPathRequestMatcher other = (AntPathRequestMatcher) obj;
            return this.pattern.equals(other.pattern) && this.httpMethod == other.httpMethod && this.caseSensitive == other.caseSensitive;
        }
    }

    public int hashCode() {
        int code = 31 ^ this.pattern.hashCode();
        if (this.httpMethod != null) {
            code ^= this.httpMethod.hashCode();
        }

        return code;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ant [pattern='").append(this.pattern).append("'");
        if (this.httpMethod != null) {
            sb.append(", ").append(this.httpMethod);
        }

        sb.append("]");
        return sb.toString();
    }

    private interface Matcher {
        boolean matches(String var1);

        Map<String, String> extractUriTemplateVariables(String var1);
    }

    private static class SubpathMatcher implements AntPathRequestMatcher.Matcher {
        private final String subpath;
        private final int length;
        private final boolean caseSensitive;

        private SubpathMatcher(String subpath, boolean caseSensitive) {
            assert !subpath.contains("*");

            this.subpath = caseSensitive ? subpath : subpath.toLowerCase();
            this.length = subpath.length();
            this.caseSensitive = caseSensitive;
        }

        public boolean matches(String path) {
            if (!this.caseSensitive) {
                path = path.toLowerCase();
            }

            return path.startsWith(this.subpath) && (path.length() == this.length || path.charAt(this.length) == '/');
        }

        public Map<String, String> extractUriTemplateVariables(String path) {
            return Collections.emptyMap();
        }
    }

    private static class SpringAntMatcher implements AntPathRequestMatcher.Matcher {
        private final AntPathMatcher antMatcher;
        private final String pattern;

        private SpringAntMatcher(String pattern, boolean caseSensitive) {
            this.pattern = pattern;
            this.antMatcher = createMatcher(caseSensitive);
        }

        private static AntPathMatcher createMatcher(boolean caseSensitive) {
            AntPathMatcher matcher = new AntPathMatcher();
            matcher.setTrimTokens(false);
            matcher.setCaseSensitive(caseSensitive);
            return matcher;
        }

        public boolean matches(String path) {
            return this.antMatcher.match(this.pattern, path);
        }

        public Map<String, String> extractUriTemplateVariables(String path) {
            return this.antMatcher.extractUriTemplateVariables(this.pattern, path);
        }
    }
}
