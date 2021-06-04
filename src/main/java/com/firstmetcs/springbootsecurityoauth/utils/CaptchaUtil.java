package com.firstmetcs.springbootsecurityoauth.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;

/**
 * @author fancunshuo
 */
public class CaptchaUtil {

    /**
     * 生成验证码图片
     *
     * @param request           设置session
     * @param response          转成图片
     * @param captchaProducer   生成图片方法类
     * @param captchaSessionKey session名称
     * @throws Exception
     */
    public static void generateCaptcha(HttpServletRequest request, HttpServletResponse response, DefaultKaptcha captchaProducer, String captchaSessionKey) throws Exception {
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        System.out.println(capText);
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(capText), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // store the text in the session
        request.getSession().setAttribute(captchaSessionKey, capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}