package com.alibaba.nacosvip.core;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacosvip.util.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Set;

/**
 * <pre>
 * </pre>
 * CacheStorage
 * Date: 2020/7/2 0002
 * Time: 17:59
 *
 * @author tang.xiaosheng@qq.com
 */
public class CacheStorage extends AbstractStorage {

    private static Cache<String, Set<String>> cache = Caffeine.newBuilder()
            .maximumSize(1)
            .build();


    @Override
    public Set<String> get() {
        return cache.getIfPresent(Constants.CACHE_NACOS_ADDRESS_LIST);
    }

    @Override
    public void post(Set<String> clusterIps) {

        Set<String> nacosAddressList = get();
        if (CollectionUtils.isEmpty(nacosAddressList)) {
            cache.put(Constants.CACHE_NACOS_ADDRESS_LIST, clusterIps);
        } else {
            nacosAddressList.addAll(clusterIps);
        }

    }

    @Override
    public void delete(Set<String> clusterIps) {

        Set<String> nacosAddressList = get();
        if (CollectionUtils.isNotEmpty(nacosAddressList)) {
            nacosAddressList.removeAll(clusterIps);
        }
    }

    @Override
    public void deleteAll() {
        if (get() != null) {
            cache.invalidate(Constants.CACHE_NACOS_ADDRESS_LIST);
        }
    }
}
