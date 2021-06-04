package com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 验证码Web身份验证详细信息
 * <p>
 * 扩展WebAuthenticationDetails 的参数，增加 flag 参数。
 * <p>
 * isCaptchaCodeRight 参数用来判断 验证码是否正确的标识 true 正确，false 不正确
 *
 * @author fancunshuo
 */
public class CaptchaWebAuthenticationDetails extends WebAuthenticationDetails {
    /*** 在构造器就初始化验证码是否正确，用于在Provider判断 */
    private final boolean isCaptchaCodeRight;

    public static final String LOGIN_CAPTCHA_CODE = "captcha";

    public boolean isCaptchaCodeRight() {
        return isCaptchaCodeRight;
    }

    public CaptchaWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String requestCaptchaCode = request.getParameter(LOGIN_CAPTCHA_CODE);
        HttpSession session = request.getSession();
        String sessionCaptchaCode = (String) session.getAttribute(LOGIN_CAPTCHA_CODE);
        // 不论校验成功还是失败，要保证session的验证码被删除
        session.removeAttribute(LOGIN_CAPTCHA_CODE);
        this.isCaptchaCodeRight = !StringUtils.isEmpty(requestCaptchaCode) && !StringUtils.isEmpty(sessionCaptchaCode)
                && requestCaptchaCode.equals(sessionCaptchaCode);
    }
}
