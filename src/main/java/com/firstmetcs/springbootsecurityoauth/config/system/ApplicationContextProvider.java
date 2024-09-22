package com.firstmetcs.springbootsecurityoauth.config.system;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 解决spring boot项目中普通类中无法注入对象问题（使用例子：(UserService)ApplicationContextProvider.getBean("userService");）
 * v2：【注：需要SpringContextHolder类支持！】加入SpringContextHolder类获取Bean，因为ApplicationContextAware在类属性中获取Bean时为Null，但是在方法内获取Bean时正常所以如果为null时使用SpringContextHolder类获取Bean
 *
 * @author mc
 * @version 2.0
 * @date 2021-7-1 15:49:59
 **/
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    /**
     * 获取ApplicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 通过name获取Bean对象
     *
     * @param name bean注册名
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    public static <T> T getBean(String name) throws BeansException {
        if (getApplicationContext() != null) {
            return (T) getApplicationContext().getBean(name);
        } else {
            return SpringContextHolder.getBean(name);
        }
    }

    /**
     * 通过class获取Bean对象
     *
     * @param c   对象类型
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> c) throws BeansException {
        if (getApplicationContext() != null) {
            return getApplicationContext().getBean(c);
        } else {
            return SpringContextHolder.getBean(c);
        }
    }

    /**
     * 通过name,以及Class获取Bean对象
     *
     * @param name bean注册名
     * @param c    对象类型
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    public static <T> T getBean(String name, Class<T> c) {
        if (getApplicationContext() != null) {
            return getApplicationContext().getBean(name, c);
        } else {
            return SpringContextHolder.getBean(name, c);
        }
    }

    public static String getString(String key) {
        return getEnvironment().getProperty(key);
    }

    public static int getInt(String key) {
        return NumberUtils.toInt(getEnvironment().getProperty(key));
    }

    public static long getLong(String key) {
        return NumberUtils.toLong(getEnvironment().getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return BooleanUtils.toBoolean(getEnvironment().getProperty(key));
    }

}