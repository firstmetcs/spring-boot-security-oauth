package com.firstmetcs.springbootsecurityoauth.config.security.oauth.granter;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.service.IntegrationUserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * custom TokenGranter Token生成器
 *
 * @author fancunshuo
 */
public class CustomTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "custom";

    protected IntegrationUserDetailsServiceImpl userDetailsService;

    private final OAuth2RequestFactory requestFactory;

    public CustomTokenGranter(IntegrationUserDetailsServiceImpl userDetailsService,
                              AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService,
                              OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
        this.requestFactory = requestFactory;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        UserDetails user = this.getUser(parameters);
        if (user == null) {
            throw new InvalidGrantException("无法获取用户信息");
        }
        OAuth2Request storedOAuth2Request = this.requestFactory.createOAuth2Request(client, tokenRequest);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(user);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request, authentication);
        return oAuth2Authentication;
    }

    private UserDetails getUser(Map<String, String> params) {
        return userDetailsService.loadUserByUsername(params.get("username"));
    }
}