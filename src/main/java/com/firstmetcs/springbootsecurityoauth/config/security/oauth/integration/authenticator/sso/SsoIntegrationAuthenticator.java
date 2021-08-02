package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.sso;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthentication;
import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 完美SSO认证器
 *
 * @author fancunshuo
 */
@Component
public class SsoIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator implements ApplicationEventPublisherAware {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SSO_AUTH_TYPE = "sso";

    @Override
    public UserDetails authenticate(IntegrationAuthentication integrationAuthentication) {
        //通过header信息查询用户
        String username = integrationAuthentication.getAuthHeaders().get("un");
        UserDetails userVo = this.userDetailsService.loadUserByUsername(username);
        return userVo;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String username = integrationAuthentication.getAuthHeaders().get("un");
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Illegal user name");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SSO_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
