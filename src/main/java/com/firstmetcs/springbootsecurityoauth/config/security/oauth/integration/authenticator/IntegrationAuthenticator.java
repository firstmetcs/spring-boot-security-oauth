package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.authenticator;

import com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration.IntegrationAuthentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author fancunshuo
 * @date 2021-4-12
 * @apiNote Created by LIQIU on 2018-3-31.
 **/
public interface IntegrationAuthenticator {

    /**
     * 处理集成认证
     *
     * @param integrationAuthentication 请求信息
     * @return UserDetails
     */
    UserDetails authenticate(IntegrationAuthentication integrationAuthentication);


    /**
     * 进行预处理
     *
     * @param integrationAuthentication 请求信息
     */
    void prepare(IntegrationAuthentication integrationAuthentication);

    /**
     * 判断是否支持集成认证类型
     *
     * @param integrationAuthentication 请求信息
     * @return boolean
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /**
     * 认证结束后执行
     *
     * @param integrationAuthentication 请求信息
     */
    void complete(IntegrationAuthentication integrationAuthentication);

}
