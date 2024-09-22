package com.firstmetcs.springbootsecurityoauth.config.system;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 解决spring boot项目中普通类中无法注入对象问题（注：在ApplicationContextProvider.getApplicationContext()返回null时使用此类获取Bean对象）
 * @author mc
 * @version 1.0
 * @date 2021-7-1 15:49:59
 */
@Component
public final class SpringContextHolder implements BeanFactoryPostProcessor {
    /**
     * Spring应用 Bean工厂
     */
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextHolder.beanFactory = beanFactory;
    }

    /**
     * 获取ConfigurableListableBeanFactory对象
     *
     * @return
     */
    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 通过name获取Bean对象
     *
     * @param name bean注册名
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 通过class获取Bean对象
     *
     * @param c 对象类型
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    public static <T> T getBean(Class c) throws BeansException {
        T result = (T) beanFactory.getBean(c);
        return result;
    }

    /**
     * 通过name,以及Class获取Bean对象
     *
     * @param name bean注册名
     * @param c 对象类型
     * @param <T>
     * @return bean实例
     * @throws BeansException
     */
    public static <T> T getBean(String name, Class c) throws BeansException {
        T result = (T) beanFactory.getBean(name, c);
        return result;
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常(NoSuchBeanDefinitionException)
     *
     * @param name
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    public static Map getBeansWithAnnotation(Class<? extends Annotation> annotationType){
        return beanFactory.getBeansWithAnnotation(annotationType);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

}
