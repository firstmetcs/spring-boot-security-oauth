package com.firstmetcs.springbootsecurityoauth.config.security.oauth.granter;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.service.OauthCasServiceImpl;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.cas.ServiceProperties;

import javax.annotation.Resource;

@Configuration
public class CasAuthenticationProviderImpl {

    @Autowired
    private ProxyGrantingTicketStorageImpl proxyGrantingTicketStorageImpl;

    @Resource
    private OauthCasServiceImpl oauthCasService;

    @Value("${cas.server.prefix}")
    private String casServerPrefix;

    @Value("${cas.client.prefix}")
    private String casClientPrefix;

    @Bean
    public ProxyGrantingTicketStorageImpl proxyGrantingTicketStorageImpl() {
        return new ProxyGrantingTicketStorageImpl();
    }

    @Bean("casAuthenticationProvider")
    @Primary
    public org.springframework.security.cas.authentication.CasAuthenticationProvider casAuthenticationProvider(@Qualifier("servicePropertiesCas") ServiceProperties servicePropertiesCas) {
        org.springframework.security.cas.authentication.CasAuthenticationProvider authenticationProvider = new org.springframework.security.cas.authentication.CasAuthenticationProvider();
        authenticationProvider.setKey("casProvider");
        authenticationProvider.setAuthenticationUserDetailsService(oauthCasService);
        Cas20ProxyTicketValidator ticketValidator = new Cas20ProxyTicketValidator(casServerPrefix);
        ticketValidator.setAcceptAnyProxy(true);//允许所有代理回调链接
        ticketValidator.setProxyGrantingTicketStorage(proxyGrantingTicketStorageImpl);
        authenticationProvider.setTicketValidator(ticketValidator);
        authenticationProvider.setServiceProperties(servicePropertiesCas);
        return authenticationProvider;
    }

    @Bean("servicePropertiesCas")
    @Primary
    public ServiceProperties servicePropertiesCas() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casClientPrefix + "/oauth/token");
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }
}
