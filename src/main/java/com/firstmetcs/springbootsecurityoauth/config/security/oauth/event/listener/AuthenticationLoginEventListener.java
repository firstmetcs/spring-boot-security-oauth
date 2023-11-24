package com.firstmetcs.springbootsecurityoauth.config.security.oauth.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class AuthenticationLoginEventListener {

    /**
     * 登陆鉴权成功事件处理
     *
     * @param event
     */
    @EventListener
    public void successEvent(AuthenticationSuccessEvent event) {
        System.out.println("-----------------用户登陆鉴权成功-----------------");
        if (!(event.getSource() instanceof UsernamePasswordAuthenticationToken)) {
            return;
        }

        if (event.getAuthentication().getDetails() != null) {
            //todo 插入用户登陆成功日志
            System.out.println("-----------------用户登陆成功-----------------");
        }
    }

    /**
     * 登陆鉴权错误事件处理
     *
     * @param event
     */
    @EventListener
    public void failureEvent(AbstractAuthenticationFailureEvent event) {
        System.out.println("-----------------插入用户登陆失败日志-----------------");

        //提供的凭据是错误的，用户名或者密码错误
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            System.out.println("---AuthenticationFailureBadCredentialsEvent---");
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            System.out.println("---AuthenticationFailureCredentialsExpiredEvent---");
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            System.out.println("---AuthenticationFailureDisabledEvent---");
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            System.out.println("---AuthenticationFailureExpiredEvent---");
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            System.out.println("---AuthenticationFailureLockedEvent---");
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            System.out.println("---AuthenticationFailureProviderNotFoundEvent---");
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            //代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            System.out.println("---AuthenticationFailureProxyUntrustedEvent---");
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            //其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            System.out.println("---AuthenticationFailureServiceExceptionEvent---");
        }
    }
}
