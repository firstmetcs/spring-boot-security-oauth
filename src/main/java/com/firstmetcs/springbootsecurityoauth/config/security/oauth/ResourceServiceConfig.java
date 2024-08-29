package com.firstmetcs.springbootsecurityoauth.config.security.oauth;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.exception.BootAccessDeniedHandler;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.exception.BootOAuth2AuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 *
 * @author fancunshuo
 */
//@Order(6)
@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private BootOAuth2AuthExceptionEntryPoint point;

    @Autowired
    private BootAccessDeniedHandler handler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("rid");
//        resources.authenticationEntryPoint(point).accessDeniedHandler(handler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/test/**").access("#oauth2.hasScope('test')");

        http.csrf().disable()
                // 统一异常处理
//                .exceptionHandling()
//                // 自定义异常返回
//                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
                // 只对 /api/** 应用 resource server 过滤
                .requestMatchers()
                // 拦截所有/api开头下的资源路径，包括其/api本身
                .antMatchers("/api/**")
                .and().authorizeRequests()

//                .antMatchers("/api/test/all").access("hasRole('ROLE_USER')")
                .anyRequest().authenticated()
//                .anyRequest()
//                // 其他请求无需认证
//                .permitAll()
        ;
    }

    // https://github.com/spring-projects/spring-security/issues/10227
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages");
        return messageSource;
    }
}