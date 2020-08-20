package com.alibaba.nacosvip.util;

import io.netty.util.internal.StringUtil;

/**
 * <pre>
 * </pre>
 * ValidationUtil
 * Date: 2019/9/11 0011
 * Time: 9:45
 *
 * @author tang.xiaosheng@qq.com
 */
public class ValidationUtil {

    /**
     * verify the validity of ip address
     *
     * @param ipAddress ip
     */
    public static boolean isCorrectIpAddress(String ipAddress) {
        if (StringUtil.isNullOrEmpty(ipAddress)) {
            return false;
        }
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        // 判断ip地址是否与正则表达式匹配
        if (ipAddress.matches(regex)) {
            String[] arr = ipAddress.split("\\.");
            for (String ip : arr) {
                int temp = Integer.parseInt(ip);
                //如果某个数字不是0到255之间的数 就返回false
                if (temp < 0 || temp > 255) return false;
            }
            return true;
        }
        return false;
    }
}
