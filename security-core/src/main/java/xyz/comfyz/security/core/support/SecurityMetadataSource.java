package xyz.comfyz.security.core.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import xyz.comfyz.security.core.util.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static xyz.comfyz.security.core.util.SecurityUtils.valueOf;

/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        13:38 2018/3/22
 * Version:     1.0
 * Description: 加载了所有requestMapping及其开方程度
 */
@Component
public class SecurityMetadataSource {

    private static Map<AntPathRequestMatcher, SecurityAuthorizeMode> requestMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initRequestMap() {
        Map<RequestMappingInfo, HandlerMethod> mapRet = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        mapRet.keySet().forEach(requestMappingInfo -> requestMappingInfo.getPatternsCondition().getPatterns().forEach(requestUrl -> {
            if (requestMappingInfo.getMethodsCondition().getMethods().isEmpty())
                requestMap.put(new AntPathRequestMatcher(StringUtils.hasText(requestUrl) ? requestUrl : "/"), SecurityAuthorizeMode.ROLE);

            requestMappingInfo.getMethodsCondition().getMethods().forEach(requestMethod ->
                    requestMap.put(new AntPathRequestMatcher(StringUtils.hasText(requestUrl) ? requestUrl : "/", requestMethod.name()), SecurityAuthorizeMode.ROLE));
        }));
    }

    public SecurityMetadataSource addAll(Map<AntPathRequestMatcher, SecurityAuthorizeMode> extra) {
        requestMap.putAll(extra);
        return this;
    }

    public static Collection<AntPathRequestMatcher> requestMapping() {
        if (CollectionUtils.isEmpty(requestMap))
            return Collections.emptyList();
        return requestMap.keySet().parallelStream().filter(matcher -> SecurityAuthorizeMode.ROLE.equals(requestMap.get(matcher))).collect(Collectors.toSet());
    }

    public SecurityAuthorizeMode getMatcher(HttpServletRequest request) {
        return requestMap.keySet().stream().filter(matcher -> matcher.matches(request))
                .map(requestMap::get)
                .sorted((o1, o2) -> Integer.compare(o2.ordinal(), o1.ordinal()))
                .findFirst().orElse(null);
    }

    public static boolean match(String url, String method) {
        return requestMap.keySet().parallelStream().anyMatch(antPathRequestMatcher ->
                (antPathRequestMatcher.getHttpMethod() == null || !StringUtils.hasText(method)
                        || antPathRequestMatcher.getHttpMethod() == valueOf(method))
                        && antPathRequestMatcher.matches(url));
    }
}
