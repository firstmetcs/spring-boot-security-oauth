package com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha;

import com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha.exception.CaptchaCodeException;
import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * 验证码认证提供者
 * <p>
 * 重写 additionalAuthenticationChecks 方法，增加图片验证码的校验判断
 * 成功，执行父类校验，走原有流程
 * 失败，抛出异常，告知用户验证码错误
 * <p>
 * 该类需要在核心配置中配置，以便于替换原有的 AuthenticationProvider
 *
 * @author fancunshuo
 */
@Component
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 使用构造方法，由Spring注入UserDetailService和PasswordEncoder
     *
     * @param userDetailsService 用户详细服务
     * @param passwordEncoder    密码加密方式
     */
    @Autowired
    public CaptchaAuthenticationProvider(UserDetailsServiceImpl userDetailsService,
                                         PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    /**
     * 验证码验证是否正确
     *
     * @param userDetails    UserDetails
     * @param authentication UsernamePasswordAuthenticationToken
     * @throws AuthenticationException CaptchaCodeException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            // 获取details，获取验证码是否通过的标识
            CaptchaWebAuthenticationDetails details = (CaptchaWebAuthenticationDetails) authentication.getDetails();
            // 如果验证码错误抛出异常
            if (!details.isCaptchaCodeRight()) {
                throw new CaptchaCodeException("Captcha error");
            }
        }
        // 成功，调用父类验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
