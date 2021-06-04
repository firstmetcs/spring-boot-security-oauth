package com.firstmetcs.springbootsecurityoauth.utils;

import com.github.pagehelper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;

/**
 * SSO单点登录工具类
 *
 * @author mc
 * @version 2.0
 * @date 2019/7/10 00:00
 **/
public class SsoUtil {

    /**
     * 系统SSO单点登录token密钥
     */
    private static final String TOKEN_SECRET = PropertiesUtil.getServiceProperty("SSO_TOKEN_SECRET");

    /**
     * uri
     */
    private String uri;
    /**
     * 实际客户端IP
     */
    private String remoteIp;
    /**
     * 用户登录名X-SSO-UID
     */
    private String ssoUid;
    /**
     * 用户员工编号X-SSO-EMPNUMBER
     */
    private String empNumber;
    /**
     * 用户邮箱X-SSO-MAIL
     */
    private String ssoMail;
    /**
     * 全名信息X-SSO-CN
     */
    private String ssoCn;
    /**
     * sso校验符X-SSO-SIGN
     */
    private String ssoSign;
    /**
     * 用户IPX-SSO-RSR-IP
     */
    private String ssoRsrIp;
    /**
     * User-Agent
     */
    private String userAgent;
    /**
     * Other备用信息
     */
    private String other;

    public SsoUtil() {
    }

    public SsoUtil(HttpServletRequest httpServletRequest) {
        setSsoInfo(httpServletRequest);
    }

    /**
     * 设置SSO单点登录信息
     *
     * @param httpServletRequest Request对象
     */
    public void setSsoInfo(HttpServletRequest httpServletRequest) {
        uri = PropertiesUtil.getServiceProperty("SERVICE_PREFIX") + httpServletRequest.getRequestURI();
        remoteIp = getRemoteIp(httpServletRequest);
        ssoUid = httpServletRequest.getHeader("X-SSO-UID") == null ? "" : httpServletRequest.getHeader("X-SSO-UID");
        empNumber = httpServletRequest.getHeader("X-SSO-EMPNUMBER") == null ? "" : httpServletRequest.getHeader("X-SSO-EMPNUMBER");
        ssoMail = httpServletRequest.getHeader("X-SSO-MAIL") == null ? "" : httpServletRequest.getHeader("X-SSO-MAIL");
        try {
            ssoCn = (httpServletRequest.getHeader("X-SSO-CN") == null ? "" : URLDecoder.decode(httpServletRequest.getHeader("X-SSO-CN"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ssoSign = httpServletRequest.getHeader("X-SSO-SIGN") == null ? "" : httpServletRequest.getHeader("X-SSO-SIGN");
        ssoRsrIp = httpServletRequest.getHeader("X-SSO-RSR-IP") == null ? "" : httpServletRequest.getHeader("X-SSO-RSR-IP");
        userAgent = httpServletRequest.getHeader("User-Agent") == null ? "" : httpServletRequest.getHeader("User-Agent");
    }

    /**
     * 获得用户登录相关用于日志记录的信息字符串(注：用于登录日志记录)
     */
    public String getLoginLog() {
        StringBuilder sbLog = new StringBuilder();
        if (StringUtil.isEmpty(ssoUid)) {
            sbLog.append("【非SSO登录】 ip=");
        } else {
            sbLog.append("【SSO登录】 ip=");
        }
        sbLog.append(remoteIp).append("; userAgent=").append(userAgent);
        if (StringUtil.isNotEmpty(ssoUid)) {
            sbLog.append("; ssoUid=").append(ssoUid)
                    .append("; empNumber=").append(empNumber)
                    .append("; ssoCn=").append(ssoCn)
                    .append("; ssoRsrIp=").append(ssoRsrIp);
        }
        return sbLog.toString();
    }

    /**
     * 获得SSO单点登录相关信息字符串(注：字符串不包含ssoMail信息)
     */
    public String getSsoInfoString() {
        return "remoteIp=" + remoteIp + "; uri=" + uri + "; ssoUid=" + ssoUid + "; empNumber=" + empNumber
                + "; ssoCn=" + ssoCn + "; ssoRsrIp=" + ssoRsrIp + "; userAgent=" + userAgent
                + "; ssoSign=" + ssoSign;
    }

    /**
     * 校验是否是SSO传来的信息
     *
     * @return 返回是否校验成功
     */
    public boolean checkSso() {
        return checkSso(0);
    }

    /**
     * 校验是否是SSO传来的信息
     *
     * @param type 类型（0不输出日志，1只是检测失败时输出日志，2检测正常和失败时都输出日志）
     * @return 返回是否校验成功
     */
    public boolean checkSso(int type) {
        boolean bIsCheck = false;
        try {
            String sTmpSsoSign = md5(uri + ssoUid + ssoRsrIp + userAgent + TOKEN_SECRET);
            bIsCheck = sTmpSsoSign.toLowerCase().equals(ssoSign);
            if (type != 0) {
                if (!bIsCheck) {
                    System.out.println("---checkSso---单点登录失败！！！信息：" + getSsoInfoString());
                } else {
                    if (type != 1) {
                        System.out.println("---checkSso---单点登录成功，信息：" + getSsoInfoString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bIsCheck;
    }

    /**
     * 获得MD5数据
     *
     * @param s 传入的字符
     * @return 返回MD5数据
     */
    public static String md5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得实际客户端IP
     *
     * @param request request对象
     * @return 实际客户端IP
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        String unKnown = "unKnown";
        if (StringUtil.isNotEmpty(ip) && !unKnown.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtil.isNotEmpty(ip) && !unKnown.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public String getUri() {
        return uri;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getSsoUid() {
        return ssoUid;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public String getSsoMail() {
        return ssoMail;
    }

    public String getSsoCn() {
        return ssoCn;
    }

    public String getSsoSign() {
        return ssoSign;
    }

    public String getSsoRsrIp() {
        return ssoRsrIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public void setSsoUid(String ssoUid) {
        this.ssoUid = ssoUid;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public void setSsoMail(String ssoMail) {
        this.ssoMail = ssoMail;
    }

    public void setSsoCn(String ssoCn) {
        this.ssoCn = ssoCn;
    }

    public void setSsoSign(String ssoSign) {
        this.ssoSign = ssoSign;
    }

    public void setSsoRsrIp(String ssoRsrIp) {
        this.ssoRsrIp = ssoRsrIp;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
