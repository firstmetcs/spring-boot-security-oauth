package com.firstmetcs.springbootsecurityoauth.config.system;

import com.firstmetcs.springbootsecurityoauth.utils.NetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Objects;

/**
 * @author lnj
 * createTime 2018-11-07 22:37
 **/
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println();
        if (swaggerEnable) {
            System.out.println("---------------------------------------------");
            System.out.println("swagger 已启动，地址为：");
            System.out.println("http://localhost:8080/swagger-ui.html");
            System.out.println("http://" + NetUtils.getRealIP() + ":8080/swagger-ui.html");
            System.out.println("---------------------------------------------");
        }
        String[] sourceArgs = args.getSourceArgs();
        for (String arg : sourceArgs) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }
}