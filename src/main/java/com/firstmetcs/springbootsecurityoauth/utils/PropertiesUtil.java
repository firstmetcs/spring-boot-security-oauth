package com.firstmetcs.springbootsecurityoauth.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取系统级和服务级配置文件工具类（使用spring框架中PropertiesLoaderUtils.loadAllProperties方法读取配置文件，用于解决无法在jar中读取配置问题）
 *
 * @author mc
 * @version 2.0
 * @date 2020-8-20 14:33:28
 **/
public class PropertiesUtil {

	private PropertiesUtil() {
	}

	private static Properties propSystem = new Properties();
	private static Properties propService = new Properties();

	private static void init() {
		try {
			//系统应用层配置文件
			propSystem = PropertiesLoaderUtils.loadAllProperties("system.properties");
			//系统服务层配置文件（正式环境测试环境不一样的配置信息再此处配置）
			propService = PropertiesLoaderUtils.loadAllProperties("service.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static {
		init();
	}

	/**
	 * 重新加载配置文件信息
	 */
	public static void reload() {
		init();
	}

	/**
	 * 读取系统应用层配置信息(system.properties)
	 * @deprecated 请使用getSystemProperty(String key)方法
	 * @param key 属性
	 * @return
	 */
	public static String getProperty(String key) {
		return getSystemProperty(key);
	}
	/**
	 * 读取系统应用层配置信息(system.properties，建议：各环境使用统一的配置信息再此处配置)
	 * @param key 属性
	 * @return
	 */
	public static String getSystemProperty(String key) {
		return propSystem.getProperty(key);
	}

	/**
	 * 读取系统服务层配置信息(service.properties，建议：各环境配置不一致的信息再此处配置)
	 * @param key 属性
	 * @return
	 */
	public static String getServiceProperty(String key) {
		return propService.getProperty(key);
	}

}