package xyz.comfyz.security.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static xyz.comfyz.security.core.support.SecurityMetadataSource.requestMapping;


/**
 * Author:      宗康飞
 * Mail:        zongkangfei@sudiyi.cn
 * Date:        18:49 2018/3/21
 * Version:     1.0
 * Description:
 */
@RestController
@RequestMapping("/security")
public class RequestMappingController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    public Collection getRequestMapping() throws IllegalArgumentException {
        return requestMapping();
    }

}
