package com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha.generator;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 谷歌图片验证码配置
 *
 * @author fancunshuo
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();

        Properties properties = new Properties();
        //图片是否有边框
        properties.setProperty(Constants.KAPTCHA_BORDER, "no");
        //图片的边框颜色
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "34,114,200");
        //图片的大小
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "125");
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "45");
        //文字内容
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "5");
        //图片背景颜色
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, "90,90,90");
        properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, "white");
        // 图片样式：
        // 水纹com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        // session的key
        properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY, "code");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}