package com.firstmetcs.springbootsecurityoauth.config.security.security.cas;

import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CasSecurityConfig {

    @Value("${cas.server.prefix}")
    private String casServerPrefix;

    @Value("${cas.server.login}")
    private String casServerLogin;

    @Value("${cas.server.logout}")
    private String casServerLogout;

    @Value("${cas.client.login}")
    private String casClientLogin;

    @Value("${cas.client.logout}")
    private String casClientLogout;

    @Value("${cas.client.relative}")
    private String casClientLogoutRelative;

    /**
     * 配置CAS Client的属性
     *
     * @return ServiceProperties
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        // 与CasAuthenticationFilter监视的URL一致
        serviceProperties.setService(casClientLogin);
        // 是否关闭单点登录，默认为false，所以也可以不设置。
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    /**
     * 配置CAS认证入口，提供用户浏览器重定向的地址
     *
     * @param serviceProperties
     * @return CasAuthenticationEntryPoint
     */
    @Bean
    @Primary
    public CasAuthenticationEntryPoint authenticationEntryPoint(ServiceProperties serviceProperties) {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        // CAS Server认证的登录地址
        entryPoint.setLoginUrl(casServerLogin);
        entryPoint.setServiceProperties(serviceProperties);
        return entryPoint;
    }

    /**
     * 配置ticket校验功能，需要提供CAS Server校验ticket的地址
     *
     * @return TicketValidator
     */
    @Bean
    public TicketValidator ticketValidator() {
        // 默认情况下使用Cas20ProxyTicketValidator，验证入口是${casServerPrefix}/proxyValidate
        return new Cas20ProxyTicketValidator(casServerPrefix);
    }

    /**
     * 设置cas认证处理逻辑
     *
     * @param serviceProperties
     * @param ticketValidator
     * @param userDetailsService
     * @return CasAuthenticationProvider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(ServiceProperties serviceProperties, TicketValidator ticketValidator, UserDetailsServiceImpl userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(ticketValidator);
        provider.setUserDetailsService(userDetailsService);
        provider.setKey("casProvider");
        return provider;
    }

    /**
     * 提供CAS认证专用过滤器，过滤器的认证逻辑由CasAuthenticationProvider提供
     *
     * @param serviceProperties
     * @param casAuthenticationProvider
     * @return CasAuthenticationFilter
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties serviceProperties, CasAuthenticationProvider casAuthenticationProvider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(serviceProperties);
        filter.setAuthenticationManager(new ProviderManager(Collections.singletonList(casAuthenticationProvider)));
        return filter;
    }

    /**
     * 配置单点登录过滤器，接受cas服务端发出的注销请求
     *
     * @return SingleSignOutFilter
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setLogoutParameterName(casServerLogout);
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * 将注销请求转发到cas server
     *
     * @return LogoutFilter
     */
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(casServerLogout, new SecurityContextLogoutHandler());
        // 设置客户端注销请求的路径
        logoutFilter.setFilterProcessesUrl(casClientLogoutRelative);
        return logoutFilter;
    }

}