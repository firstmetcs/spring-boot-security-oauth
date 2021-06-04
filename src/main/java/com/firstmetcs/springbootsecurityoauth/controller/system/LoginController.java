package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.utils.CaptchaUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fancunshuo
 */
@Controller
public class LoginController {

    public static final String LOGIN_CAPTCHA_CODE = "captcha";

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping("/login-page")
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/login-captcha-code"})
    public void loginCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.generateCaptcha(request, response, defaultKaptcha, LOGIN_CAPTCHA_CODE);
    }
}
