package com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常
 *
 * @author fancunshuo
 */
public class CaptchaCodeException extends AuthenticationException {
    public CaptchaCodeException(String msg) {
        super(msg);
    }
}
