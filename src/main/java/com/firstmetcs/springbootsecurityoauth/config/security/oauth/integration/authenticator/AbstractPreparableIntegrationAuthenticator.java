package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author fancunshuo
 * @date 2021-4-12
 * @apiNote Created by LIQIU on 2018-4-4.
 **/
public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator {

    @Override
    public abstract UserDetails authenticate(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
