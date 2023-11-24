package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.DefaultOAuthClientAuthentication;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.IntegrationAuthenticator;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 拦截器拦截登录请求
 *
 * @author fancunshuo
 * @date 2021-4-12
 * @apiNote Created by LIQIU on 2018-3-30.
 **/
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private static final String AUTH_TYPE_PARAM_NAME = "auth_type";

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private Collection<IntegrationAuthenticator> authenticators;

    private ApplicationContext applicationContext;

    private final RequestMatcher requestMatcher;

    @Autowired
    private AuthenticationEventPublisher authenticationEventPublisher;

    public IntegrationAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (requestMatcher.matches(request)) {
            //设置集成登录信息
            IntegrationAuthentication integrationAuthentication = new IntegrationAuthentication();
            integrationAuthentication.setAuthType(request.getParameter(AUTH_TYPE_PARAM_NAME));
            integrationAuthentication.setAuthParameters(request.getParameterMap());
            integrationAuthentication.setAuthHeaders(getHeadersInfo(request));
            integrationAuthentication.setAccessIp(getRemoteIp(request));
            IntegrationAuthenticationContext.set(integrationAuthentication);
            try {
                //预处理
                this.prepare(integrationAuthentication);

                filterChain.doFilter(request, response);

                //后置处理
                this.complete(integrationAuthentication);
            } finally {
                IntegrationAuthenticationContext.clear();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 进行预处理
     *
     * @param integrationAuthentication 请求信息
     */
    private void prepare(IntegrationAuthentication integrationAuthentication) {

        //延迟加载认证器
        if (this.authenticators == null) {
            synchronized (this) {
                Map<String, IntegrationAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(IntegrationAuthenticator.class);
                if (integrationAuthenticatorMap != null) {
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }

        for (IntegrationAuthenticator authenticator : authenticators) {
            if (authenticator.support(integrationAuthentication)) {
                try {
                    authenticator.prepare(integrationAuthentication);
                } catch (AuthenticationException ex) {
                    authenticationEventPublisher.publishAuthenticationFailure(ex, new DefaultOAuthClientAuthentication());
                    throw ex;
                }
            }
        }
    }

    /**
     * 后置处理
     *
     * @param integrationAuthentication 请求信息
     */
    private void complete(IntegrationAuthentication integrationAuthentication) {
        for (IntegrationAuthenticator authenticator : authenticators) {
            if (authenticator.support(integrationAuthentication)) {
                authenticator.complete(integrationAuthentication);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * get request headers
     * 获取request请求头
     *
     * @param request request请求
     * @return Map<String, String>
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * filter将手动注册到SpringSecurity。
     * 但SpringBoot会自动注册任何一个bean组件，这样导致注册两次从而每次调用都会 触发两次，
     * 故通过此来让SpringBoot别自动注册此filter
     *
     * @param integrationAuthenticationFilter 过滤器
     * @return FilterRegistrationBean<IntegrationAuthenticationFilter>
     */
    @Bean
    public FilterRegistrationBean<IntegrationAuthenticationFilter> registration(IntegrationAuthenticationFilter integrationAuthenticationFilter) {
        FilterRegistrationBean<IntegrationAuthenticationFilter> registration = new FilterRegistrationBean<>(integrationAuthenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 获得实际客户端IP
     *
     * @param request request对象
     * @return 实际客户端IP
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        String unKnown = "unKnown";
        if (StringUtil.isNotEmpty(ip) && !unKnown.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtil.isNotEmpty(ip) && !unKnown.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}