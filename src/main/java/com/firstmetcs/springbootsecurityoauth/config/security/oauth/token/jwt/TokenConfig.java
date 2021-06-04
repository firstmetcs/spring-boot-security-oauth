package com.firstmetcs.springbootsecurityoauth.config.security.oauth.token.jwt;//package com.firstmetcs.springbootsecurity.config.security.oauth.token.jwt;
//
//import com.firstmetcs.springbootsecurity.config.security.security.service.UserDetailsServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//@Configuration
//public class TokenConfig {
//
//    @Autowired
//    UserDetailsServiceImpl userDetailsService;
//
//    @Bean
//    public TokenStore tokenStore() {
//        //jwt管理令牌
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    // JWT 签名秘钥
//    private static final String SIGNING_KEY = "wj-key";
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
//        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//        DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
//        userTokenConverter.setUserDetailsService(userDetailsService);
//        accessTokenConverter.setUserTokenConverter(userTokenConverter);
//
//        jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
//        return jwtAccessTokenConverter;
//    }
//}
