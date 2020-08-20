package com.alibaba.nacosvip.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <pre>
 * </pre>
 * ApplicationUtils
 * Date: 2020/7/3 0003
 * Time: 14:21
 *
 * @author tang.xiaosheng@qq.com
 */
public class ApplicationUtils implements ApplicationContextInitializer {
    private static ConfigurableEnvironment environment;
    private static ApplicationContext applicationContext;
    private static Boolean isStandalone = null;
    private static Boolean isSetRedis = null;

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * startup mode
     */
    public static boolean getStandaloneMode() {
        if (Objects.isNull(isStandalone)) {
            isStandalone = Boolean.parseBoolean(getProperty(Constants.STANDALONE));
        }
        return isStandalone;
    }

    /**
     * is set redis link
     */
    public static boolean isSetRedis() {
        if (Objects.isNull(isSetRedis)) {
            isSetRedis = !StringUtils.isEmpty(getProperty(Constants.REDIS_HOST));
        }
        return isSetRedis;
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        environment = configurableApplicationContext.getEnvironment();
        applicationContext = configurableApplicationContext;
    }
}
