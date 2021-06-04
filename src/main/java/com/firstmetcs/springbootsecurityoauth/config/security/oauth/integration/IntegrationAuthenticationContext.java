package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration;

/**
 * 请求信息上下文
 *
 * @author fancunshuo
 * @date 2021-4-12
 * @apiNote Created by LIQIU on 2018-3-30.
 **/
public class IntegrationAuthenticationContext {

    private static final ThreadLocal<IntegrationAuthentication> HOLDER = new ThreadLocal<>();

    public static void set(IntegrationAuthentication integrationAuthentication) {
        HOLDER.set(integrationAuthentication);
    }

    public static IntegrationAuthentication get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
