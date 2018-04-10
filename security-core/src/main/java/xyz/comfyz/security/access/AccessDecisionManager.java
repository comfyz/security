package xyz.comfyz.security.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.comfyz.security.access.basic.SecurityMetadataSource;
import xyz.comfyz.security.model.AuthenticationToken;
import xyz.comfyz.security.model.SecurityAuthorizeMode;
import xyz.comfyz.security.util.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        11:09 2018/3/22
 * Version:     1.0
 * Description: 判断当前用户{@link AuthenticationToken}是否有访问该资源{@link HttpServletRequest}的权限{@link AntPathRequestMatcher}
 */
@Component
public class AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDecisionManager.class);
    private final SecurityMetadataSource securityMetadataSource;

    @Autowired
    public AccessDecisionManager(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    boolean decide(AuthenticationToken authenticationToken, HttpServletRequest request) {
        if (authenticationToken != null && authenticationToken.getUserDetails() != null && authenticationToken.getUserDetails().isAdmin())
            return true;

        SecurityAuthorizeMode authorizeMode = securityMetadataSource.getMatcher(request);
        if (authorizeMode != null)
            switch (authorizeMode) {
                case NONE:
                    break;
                case AUTHENTICATED:
                    if (authenticationToken == null || authenticationToken.getUserDetails() == null)
                        return false;
                    break;
                case ROLE:
                    if (authenticationToken == null
                            || authenticationToken.getUserDetails() == null
                            || CollectionUtils.isEmpty(authenticationToken.getAuthorities())
                            || authenticationToken.getAuthorities().stream().noneMatch(o -> o.getRequestMatcher().matches(request)))
                        return false;
                    break;
                default:
            }
        return true;
    }
}