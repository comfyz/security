package xyz.comfyz.security.core.access;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.comfyz.security.core.cache.UserDetailsCache;
import xyz.comfyz.security.core.common.SecurityContext;
import xyz.comfyz.security.core.support.AuthenticationTokenProvider;
import xyz.comfyz.security.core.model.AuthenticationToken;
import xyz.comfyz.security.core.util.AntPathRequestMatcher;

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
    private AntPathRequestMatcher matcher = new AntPathRequestMatcher("/*");

    @Autowired
    private AuthenticationTokenProvider authenticationTokenProvider;
    @Autowired
    private AccessDecisionManager accessDecisionManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        cacheRequestResponse(servletRequest, servletResponse);
        if (matcher.matches((HttpServletRequest) servletRequest)) {
            //获取用户
            AuthenticationToken token = authenticationTokenProvider.loadUser((HttpServletRequest) servletRequest);
            //校验用户是否有权限
            if (accessDecisionManager.decide(token, (HttpServletRequest) servletRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            //403
            LOGGER.info("Forbidden for user:{} at path:{}",
                    token == null || token.getUserDetails() == null ? "none" : token.getUserDetails().getUserName(),
                    ((HttpServletRequest) servletRequest).getRequestURI());
            JSONObject object = new JSONObject();
            object.put("code", "403");
            object.put("msg", String.format("Forbidden for user:\"%s\" at path:\"%s\"",
                    token == null || token.getUserDetails() == null ? "none" : token.getUserDetails().getUserName(),
                    ((HttpServletRequest) servletRequest).getRequestURI()));
            servletResponse.getWriter().write(object.toString());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void cacheRequestResponse(ServletRequest request, ServletResponse response) {
        SecurityContext.set((HttpServletRequest) request);
        SecurityContext.set((HttpServletResponse) response);
    }

    public void setMatcher(AntPathRequestMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    public void userDetailsCache(UserDetailsCache userDetailsCache) {
        this.authenticationTokenProvider.userDetailsCache(userDetailsCache);
    }

}
