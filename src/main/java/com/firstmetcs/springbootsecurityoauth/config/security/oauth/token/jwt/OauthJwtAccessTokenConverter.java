package com.firstmetcs.springbootsecurityoauth.config.security.oauth.token.jwt;

import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author fancunshuo
 */
public class OauthJwtAccessTokenConverter extends JwtAccessTokenConverter {
    public OauthJwtAccessTokenConverter(UserDetailsServiceImpl userService) {
        super.setAccessTokenConverter(new OauthAccessTokenConverter(userService));
    }
}
