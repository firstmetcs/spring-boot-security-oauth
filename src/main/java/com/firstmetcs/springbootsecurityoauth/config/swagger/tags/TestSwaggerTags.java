package com.firstmetcs.springbootsecurityoauth.config.swagger.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger Api 描述配置
 *
 * @author Administrator
 */
@Configuration
public class TestSwaggerTags {

    public static final String TEST = "Test Controller";

    @Autowired
    public TestSwaggerTags(@Qualifier("testDocket") Docket systemDocket) {
        systemDocket.tags(
                new Tag(TEST, "测试案例")
        );
    }
}
