package com.firstmetcs.springbootsecurityoauth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fancunshuo
 */
public class HttpUtils {

    public static void writerError(String message, HttpServletResponse response) throws IOException {
        response.setContentType("application/json,charset=utf-8");
//        response.setStatus(bs.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), message);
    }

}
