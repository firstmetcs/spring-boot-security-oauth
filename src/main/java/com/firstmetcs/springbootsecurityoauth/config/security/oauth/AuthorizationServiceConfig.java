package com.firstmetcs.springbootsecurityoauth.config.security.oauth;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthenticationFilter;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.granter.CustomTokenGranter;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.service.IntegrationUserDetailsServiceImpl;
import com.firstmetcs.springbootsecurityoauth.config.security.oauth.exception.WebResponseExceptionTranslator;
import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


/**
 * 认证服务器配置
 *
 * @author fancunshuo
 */
@Order(8)
@Configuration
@EnableAuthorizationServer
public class AuthorizationServiceConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private IntegrationAuthenticationFilter integrationAuthenticationFilter;
    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;
    @Autowired
    private IntegrationUserDetailsServiceImpl integrationUserDetailsService;
    @Resource
    private DataSource dataSource;
    @Autowired
    private TokenStore tokenStore;
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 这个是定义授权的请求的路径的Bean
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置客户端详细服务， 客户端的详情在这里进行初始化
     *
     * @param clients ClientDetailsServiceConfigurer
     * @throws Exception Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //默认值InMemoryTokenStore对于单个服务器是完全正常的（即，在发生故障的情况下，低流量和热备份备份服务器）。大多数项目可以从这里开始，也可以在开发模式下运行，以便轻松启动没有依赖关系的服务器。
        //这JdbcTokenStore是同一件事的JDBC版本，它将令牌数据存储在关系数据库中。如果您可以在服务器之间共享数据库，则可以使用JDBC版本，如果只有一个，则扩展同一服务器的实例，或者如果有多个组件，则授权和资源服务器。要使用JdbcTokenStore你需要“spring-jdbc”的类路径。

        //这个地方指的是从jdbc查出数据来存储
        clients.withClientDetails(clientDetails());


    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务（token services）
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenGranter(new CompositeTokenGranter(this.getTokenGranters(endpoints.getClientDetailsService(), endpoints.getTokenServices(), endpoints.getAuthorizationCodeServices(), endpoints.getOAuth2RequestFactory())));
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore)
//                .tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter)
//                .exceptionTranslator(webResponseExceptionTranslator)
                .reuseRefreshTokens(true);
//        endpoints.exceptionTranslator(oAuth2ExceptionTranslator());
        endpoints.pathMapping("/oauth/confirm_access", "/custom/confirm_access");
    }

    private List<TokenGranter> getTokenGranters(ClientDetailsService clientDetails, AuthorizationServerTokenServices tokenServices, AuthorizationCodeServices authorizationCodeServices, OAuth2RequestFactory requestFactory) {
        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        // 添加授权码模式
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory));
        // 添加刷新令牌的模式
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        // 添加隐式授权模式
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory));
        // 添加客户端模式
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            // 添加密码模式
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
        }
        // 添加自定义授权模式
        tokenGranters.add(new CustomTokenGranter(integrationUserDetailsService, tokenServices, clientDetails, requestFactory));

        return tokenGranters;
    }

    /**
     * 配置令牌端点（Token Endpoint）的安全约束
     *
     * @param security AuthorizationServerSecurityConfigurer
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(integrationAuthenticationFilter);
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
//                .checkTokenAccess("isAuthenticated()");
    }
}