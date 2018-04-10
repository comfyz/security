package xyz.comfyz.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.comfyz.security.access.basic.SecurityMetadataSource;

import java.util.Collection;


/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:49 2018/3/21
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping("/request")
public final class RequestMappingController {

    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    public Collection getRequestMapping() throws IllegalArgumentException {
        return SecurityMetadataSource.requestMapping();
    }

}
