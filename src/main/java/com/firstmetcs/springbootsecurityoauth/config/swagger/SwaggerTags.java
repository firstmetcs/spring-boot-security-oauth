package com.firstmetcs.springbootsecurityoauth.config.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger Api 描述配置
 *
 * @author Administrator
 */
@Configuration
public class SwaggerTags {

    public static final String TEST = "Test Controller";
    public static final String USER = "User Controller";
    public static final String HELLO = "Hello Controller";
    public static final String GUEST = "Guest Controller";
    public static final String AUTHORITY = "Authority Controller";
    public static final String ROLE = "Role Controller";
    public static final String ROUTE = "Route Controller";
    public static final String API = "Api Controller";
    public static final String PERMISSION = "Permission Controller";

    @Autowired
    public SwaggerTags(Docket docket) {
        docket.tags(
                new Tag(TEST, "测试案例"),
                new Tag(USER, "用户管理"),
                new Tag(HELLO, "你好案例"),
                new Tag(GUEST, "访客案例"),
                new Tag(AUTHORITY, "权限管理"),
                new Tag(ROLE, "角色管理"),
                new Tag(ROUTE, "路由管理"),
                new Tag(API, "接口管理"),
                new Tag(PERMISSION, "管理")
        );
    }
}
