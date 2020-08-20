package com.alibaba.nacosvip.controller;

import java.util.Set;

/**
 * <pre>
 * </pre>
 * NacosCo
 * Date: 2019/9/6 0006
 * Time: 15:48
 *
 * @author tang.xiaosheng@qq.com
 */
public class NacosVipCO {

    /**
     * cluster ip list
     */
    private Set<String> clusterIps;

    public Set<String> getClusterIps() {
        return clusterIps;
    }

    public void setClusterIps(Set<String> clusterIps) {
        this.clusterIps = clusterIps;
    }
}
