package com.firstmetcs.springbootsecurityoauth.config.security.oauth.integration;

import lombok.Data;

import java.util.Map;

/**
 * 请求信息
 *
 * @author fancunshuo
 * @date 2021-4-12
 * @apiNote Created by LIQIU on 2018-3-30.
 **/
@Data
public class IntegrationAuthentication {

    private String authType;
    private String username;
    private String accessIp;
    private Map<String, String[]> authParameters;
    private Map<String, String> authHeaders;

    public String getAuthParameter(String parameter) {
        String[] values = this.authParameters.get(parameter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
}
