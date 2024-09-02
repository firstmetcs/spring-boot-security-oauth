package com.firstmetcs.springbootsecurityoauth.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

/**
 * swagger 核心配置类
 *
 * @author fancunshuo
 */
@Configuration
@EnableSwagger2  // 声明为swagger
public class SwaggerConfig {
    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Value("${cas.client.prefix}")
    private String casClientPrefix;

    @Value("${cas.client.token}")
    private String casClientToken;

    @Value("${cas.client.authorize}")
    private String casClientAuthorize;

    @Primary
    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(apiInfo())
                .groupName("全部")
                .select()
                // 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.firstmetcs.springbootsecurityoauth.controller"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 整合oauth2
                .securitySchemes(Arrays.asList(resourceOwnerPasswordCredentialsSecuritySchemes(), clientCredentialsSecuritySchemes(), authorizationCodeSecuritySchemes(), apiKey()))
                .securityContexts(Collections.singletonList(securityContexts()));

    }

    @Bean("systemDocket")
    public Docket systemDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(apiInfo())
                .groupName("系统管理")
                .select()
                // 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.firstmetcs.springbootsecurityoauth.controller.system"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 整合oauth2
                .securitySchemes(Arrays.asList(resourceOwnerPasswordCredentialsSecuritySchemes(), clientCredentialsSecuritySchemes(), authorizationCodeSecuritySchemes(), apiKey()))
                .securityContexts(Collections.singletonList(securityContexts()));

    }

    @Bean("testDocket")
    public Docket testDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(apiInfo())
                .groupName("测试")
                .select()
                // 设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.firstmetcs.springbootsecurityoauth.controller.test"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 整合oauth2
                .securitySchemes(Arrays.asList(resourceOwnerPasswordCredentialsSecuritySchemes(), clientCredentialsSecuritySchemes(), authorizationCodeSecuritySchemes(), apiKey()))
                .securityContexts(Collections.singletonList(securityContexts()));

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档说明
                .title("API接口文档")
                // 文档描述
                .description("Swagger2 api文档")
                // 文档版本
                .version("1.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("Spring Security Swagger", casClientPrefix + "/swagger-ui.html", null))
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(StringUtils.EMPTY)
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey("Authorization Bearer", "Authorization", "header");
    }

    /**
     * 认证方式使用密码模式
     */
    private SecurityScheme resourceOwnerPasswordCredentialsSecuritySchemes() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(casClientToken);

        return new OAuthBuilder()
                .name("ResourceOwnerPassword Authorization")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    /**
     * 认证方式使用客户端模式
     */
    private SecurityScheme clientCredentialsSecuritySchemes() {
        GrantType grantType = new ClientCredentialsGrant(casClientToken);

        return new OAuthBuilder()
                .name("Client Authorization")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    /**
     * 认证方式使用授权码模式
     */
    private SecurityScheme authorizationCodeSecuritySchemes() {
        GrantType grantType = new AuthorizationCodeGrant(new TokenRequestEndpoint(casClientAuthorize, "login",
                "Norma1-login"),
                new TokenEndpoint(casClientToken, "JWT-TOKEN"));

        return new OAuthBuilder()
                .name("AuthorizationCode Authorization")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("login")
                .clientSecret("Norma1-login")
//                .realm("test-app-realm")
//                .appName("test-app")
                .scopeSeparator(" ")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }

    /**
     * 设置 swagger2 认证的安全上下文
     */
    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("ResourceOwnerPassword Authorization", scopes()), new SecurityReference("Client Authorization", scopes()), new SecurityReference("AuthorizationCode Authorization", scopes()), new SecurityReference("Bearer", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * 设置认证的作用域scope
     *
     * @return AuthorizationScope[]
     */
    private AuthorizationScope[] scopes() {
        AuthorizationScope authorizationScopeAll = new AuthorizationScope("all", "默认所有");
        AuthorizationScope authorizationScopeRead = new AuthorizationScope("read", "读取数据");
        AuthorizationScope authorizationScopeTest = new AuthorizationScope("test", "测试");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
        authorizationScopes[0] = authorizationScopeAll;
        authorizationScopes[1] = authorizationScopeRead;
        authorizationScopes[2] = authorizationScopeTest;
        return authorizationScopes;
    }

}