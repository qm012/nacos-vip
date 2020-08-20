package com.alibaba.nacosvip.core;

import java.util.Set;

/**
 * <pre>
 * </pre>
 * Storage
 * Date: 2020/7/2 0002
 * Time: 17:54
 *
 * @author tang.xiaosheng@qq.com
 */
public interface Storage {

    /**
     * get nacos cluster server list
     *
     * @return nacos cluster server list
     */
    Set<String> get();

    /**
     * add nacos cluster server list
     *
     * @param clusterIps add param
     */
    void post(Set<String> clusterIps);

    /**
     * delete nacos cluster server list
     *
     * @param clusterIps delete param
     */
    void delete(Set<String> clusterIps);

    /**
     * delete all data
     */
    void deleteAll();
}

