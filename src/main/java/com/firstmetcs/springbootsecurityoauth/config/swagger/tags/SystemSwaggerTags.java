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
public class SystemSwaggerTags {

    public static final String USER = "User Controller";
    public static final String AUTHORITY = "Authority Controller";
    public static final String ROLE = "Role Controller";
    public static final String ROUTE = "Route Controller";
    public static final String API = "Api Controller";

    @Autowired
    public SystemSwaggerTags(@Qualifier("systemDocket") Docket systemDocket) {
        systemDocket.tags(
                new Tag(USER, "用户管理"),
                new Tag(AUTHORITY, "权限管理"),
                new Tag(ROLE, "角色管理"),
                new Tag(ROUTE, "路由管理"),
                new Tag(API, "接口管理")
        );
    }
}
