package xyz.comfyz.security.access;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.model.Authority;
import xyz.comfyz.security.model.UserDetalis;
import xyz.comfyz.security.provider.AuthenticationProvider;
import xyz.comfyz.security.provider.TokenProvider;
import xyz.comfyz.security.support.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        14:09 2018/3/26
 * Version:     1.0
 * Description:
 */
@Component
public final class SecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
    private final AuthenticationProvider authenticationProvider;
    private final AccessDecisionManager accessDecisionManager;
    private AntPathRequestMatcher matcher = new AntPathRequestMatcher("/**");

    @Autowired
    public SecurityFilter(AuthenticationProvider authenticationProvider, AccessDecisionManager accessDecisionManager) {
        this.authenticationProvider = authenticationProvider;
        this.accessDecisionManager = accessDecisionManager;
    }

    public void setMatcher(String pattern) {
        if (StringUtils.hasText(pattern))
            this.matcher = new AntPathRequestMatcher(pattern);
    }

    public void setMatcher(AntPathRequestMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            SecurityContext.cacheRequestResponse((HttpServletRequest) request, (HttpServletResponse) response);
            final UserDetalis userDetalis = SecretUtils.decrypt(TokenProvider.get());
            //判断权限
            if (matcher.matches((HttpServletRequest) request)) {
                //获取用户
                Authentication<?, Authority> token = null;
                if (userDetalis != null &&
                        (StringUtils.hasText(userDetalis.getUserId()) || !SecurityUser.ANONYMOUS.name().equals(userDetalis.getUserName())))
                    token = authenticationProvider.authenticate(userDetalis);

                if (accessDecisionManager.decide(token, (HttpServletRequest) request)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                JSONObject resp = new JSONObject();
                if (token == null || token.getUserDetails() == null) {
                    //401 UNAUTHORIZED
                    LOGGER.info("Unauthenticated at path {}", SecurityUtils.getRequestPath());
                    ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.put("code", HttpStatus.UNAUTHORIZED.value());
                    resp.put("type", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    resp.put("msg", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                } else {
                    //403 FORBIDDEN
                    LOGGER.info("Forbidden for user:{} at path:{}",
                            token.getUserDetails().getUserName(), SecurityUtils.getRequestPath());
                    ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
                    resp.put("code", HttpStatus.FORBIDDEN.value());
                    resp.put("type", HttpStatus.FORBIDDEN.getReasonPhrase());
                    resp.put("msg", String.format("Forbidden for user:\"%s\" at path:\"%s\"",
                            token.getUserDetails().getUserName(), SecurityUtils.getRequestPath()));
                }
                response.getWriter().write(resp.toString());
                return;
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            //401 UNAUTHORIZED
            JSONObject resp = new JSONObject();
            LOGGER.info("Unauthenticated at path {}", SecurityUtils.getRequestPath());
            ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.put("code", HttpStatus.UNAUTHORIZED.value());
            resp.put("type", " Authenticate failed");
            resp.put("msg", e.getLocalizedMessage());
            response.getWriter().write(resp.toString());
        } finally {
            /*
              ThreadLocal 缓存 HttpServletRequest, HttpServletResponse, Authentication
              spring容器采用线程池处理http请求，默认10个线程，导致第11次请求时, ThreadLocal 值存在且与第1次请求的存放的值相同
              因此每次需要手动清除一下
             */
            SecurityContext.clear();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}
