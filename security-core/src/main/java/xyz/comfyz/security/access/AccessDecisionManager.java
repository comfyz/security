package xyz.comfyz.security.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.comfyz.security.access.basic.SecurityMetadataSource;
import xyz.comfyz.security.model.Authentication;
import xyz.comfyz.security.model.Authority;
import xyz.comfyz.security.model.SecurityAuthorizeMode;
import xyz.comfyz.security.support.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        11:09 2018/3/22
 * Version:     1.0
 * Description: 判断当前用户{@link Authentication}是否有访问该资源{@link HttpServletRequest}的权限{@link AntPathRequestMatcher}
 */
@Component
public class AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDecisionManager.class);
    private final SecurityMetadataSource securityMetadataSource;

    @Autowired
    public AccessDecisionManager(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    boolean decide(Authentication<?, Authority> authentication, HttpServletRequest request) {
        if (authentication != null && authentication.getUserDetails() != null && authentication.getUserDetails().isAdmin())
            return true;

        SecurityAuthorizeMode authorizeMode = securityMetadataSource.getMatcher(request);
        if (authorizeMode != null)
            switch (authorizeMode) {
                case NONE:
                    break;
                case AUTHENTICATED:
                    if (authentication == null || authentication.getUserDetails() == null)
                        return false;
                    break;
                case ROLE:
                    if (authentication == null
                            || authentication.getUserDetails() == null
                            || CollectionUtils.isEmpty(authentication.getAuthorities())
                            || authentication.getAuthorities().stream().noneMatch(o -> o.getRequestMatcher().matches(request)))
                        return false;
                    break;
                default:
            }
        return true;
    }
}