package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator;

import com.alibaba.druid.util.StringUtils;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthentication;
import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public UserDetails authenticate(IntegrationAuthentication integrationAuthentication) {
        return userDetailsService.loadUserByUsername(integrationAuthentication.getUsername());
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}
