package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class DefaultOAuthClientAuthentication extends AbstractAuthenticationToken {

    public DefaultOAuthClientAuthentication() {
        super(null);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return "UNKNOWN";
    }

}