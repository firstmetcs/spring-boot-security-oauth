package com.firstmetcs.springbootsecurityoauth.config.security.oauth.service;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthenticationContext;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthentication;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.DefaultOAuthClientAuthentication;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator.IntegrationAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 处理custom类型的用户信息
 *
 * @author fancunshuo
 */
@Service
public class IntegrationUserDetailsServiceImpl implements UserDetailsService {

    private List<IntegrationAuthenticator> authenticators;

    @Autowired
    private AuthenticationEventPublisher authenticationEventPublisher;

    private final AccountStatusUserDetailsChecker accountStatusUserDetailsChecker = new AccountStatusUserDetailsChecker();

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }
        integrationAuthentication.setUsername(username);
        UserDetails userVO = null;
        try {
            userVO = this.authenticate(integrationAuthentication);
            accountStatusUserDetailsChecker.check(userVO);
        } catch (AuthenticationException ex) {
            authenticationEventPublisher.publishAuthenticationFailure(ex, new DefaultOAuthClientAuthentication());
            throw ex;
        }

        if (userVO == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return userVO;

    }

    /**
     * 执行实际认证过程
     *
     * @param integrationAuthentication 授权信息
     * @return 用户信息
     */
    private UserDetails authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}