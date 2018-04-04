package xyz.comfyz.security.core.access;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.model.UserDetalis;
import xyz.comfyz.security.core.provider.AuthenticationProvider;
import xyz.comfyz.security.core.provider.TokenProvider;
import xyz.comfyz.security.core.support.SecurityContext;
import xyz.comfyz.security.core.util.AntPathRequestMatcher;
import xyz.comfyz.security.core.util.SecretUtils;
import xyz.comfyz.security.core.util.SecurityUtils;

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
public class SecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
    private final AuthenticationProvider authenticationTokenProvider;
    private final AccessDecisionManager accessDecisionManager;
    private AntPathRequestMatcher matcher = new AntPathRequestMatcher("/**");

    public SecurityFilter(AuthenticationProvider authenticationTokenProvider, AccessDecisionManager accessDecisionManager) {
        this.authenticationTokenProvider = authenticationTokenProvider;
        this.accessDecisionManager = accessDecisionManager;
    }

    public void setMatcher(AntPathRequestMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            SecurityContext.cacheRequestResponse((HttpServletRequest) request, (HttpServletResponse) response);
            UserDetalis userDetalis = SecretUtils.decrypt(TokenProvider.get());
            //判断权限
            if (matcher.matches((HttpServletRequest) request)) {
                //获取用户
                AuthenticationToken token = authenticationTokenProvider.authenticate(userDetalis);

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
                    resp.put("msg", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                } else {
                    //403 FORBIDDEN
                    LOGGER.info("Forbidden for user:{} at path:{}",
                            token.getUserDetails().getUserName(), SecurityUtils.getRequestPath());
                    ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
                    resp.put("code", HttpStatus.FORBIDDEN.value());
                    resp.put("msg", String.format("Forbidden for user:\"%s\" at path:\"%s\"",
                            token.getUserDetails().getUserName(), SecurityUtils.getRequestPath()));
                }
                response.getWriter().write(resp.toString());
                return;
            }

            filterChain.doFilter(request, response);

        } finally {
            /*
              ThreadLocal 缓存 HttpServletRequest, HttpServletResponse, AuthenticationToken
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
