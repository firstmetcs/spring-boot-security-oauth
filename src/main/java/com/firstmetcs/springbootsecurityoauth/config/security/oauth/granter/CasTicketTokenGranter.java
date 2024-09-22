package com.firstmetcs.springbootsecurityoauth.config.security.oauth.granter;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.service.OauthCasServiceImpl;
import com.firstmetcs.springbootsecurityoauth.config.system.ApplicationContextProvider;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.springframework.security.authentication.*;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class CasTicketTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "cas_ticket";

    protected OauthCasServiceImpl userDetailsService;

    public CasTicketTokenGranter(OauthCasServiceImpl userDetailsService, AuthenticationManager authenticationManager,
                                 AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
    }

    protected CasTicketTokenGranter(OauthCasServiceImpl userDetailsService, AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                    ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        OAuth2AccessToken token = super.grant(grantType, tokenRequest);
        if (token != null) {
            DefaultOAuth2AccessToken norefresh = new DefaultOAuth2AccessToken(token);
            // The spec says that cas ticket should not be allowed to get a refresh token
            norefresh.setRefreshToken(null);
            token = norefresh;
        }
        return token;
    }

    public ServiceProperties serviceProperties(String url) {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(url);
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    public ProxyGrantingTicketStorageImpl proxyGrantingTicketStorageImpl() {
        return new ProxyGrantingTicketStorageImpl();
    }


    public org.springframework.security.cas.authentication.CasAuthenticationProvider casAuthenticationProvider(String url) {

        String casServerUrlPrefix = ApplicationContextProvider.getString("cas.server.prefix");

        org.springframework.security.cas.authentication.CasAuthenticationProvider authenticationProvider = new org.springframework.security.cas.authentication.CasAuthenticationProvider();
        authenticationProvider.setKey("casProvider");
        authenticationProvider.setAuthenticationUserDetailsService(userDetailsService);
        Cas20ProxyTicketValidator ticketValidator = new Cas20ProxyTicketValidator(casServerUrlPrefix);
        ticketValidator.setAcceptAnyProxy(true);//允许所有代理回调链接
        ticketValidator.setProxyGrantingTicketStorage(proxyGrantingTicketStorageImpl());
        authenticationProvider.setTicketValidator(ticketValidator);
        authenticationProvider.setServiceProperties(serviceProperties(url));
        return authenticationProvider;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = CasAuthenticationFilter.CAS_STATEFUL_IDENTIFIER;
        String password = parameters.get("ticket");

        if (password == null) {
            throw new InvalidRequestException("A cas ticket must be supplied.");
        }
        String url = parameters.get("url");

        if (url == null) {
            throw new InvalidRequestException("A cas url must be supplied.");
        }

        CasAuthenticationProvider authenticationManager = casAuthenticationProvider(url);

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException e) {
            // If the ticket is wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate ticket: " + password);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
