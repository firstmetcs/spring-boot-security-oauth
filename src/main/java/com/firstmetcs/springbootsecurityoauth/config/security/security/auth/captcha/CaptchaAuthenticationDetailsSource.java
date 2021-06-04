package com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码Web身份验证详细信息源
 * <p>
 * 用于替换 UsernamePasswordAuthenticationFilter 中默认的  AuthenticationDetailsSource 属性
 * 此类需要在核心配置中配置
 * 代码示例：
 * <p>
 *
 * @author fancunshuo
 */
@Component
public class CaptchaAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new CaptchaWebAuthenticationDetails(request);
    }
}
