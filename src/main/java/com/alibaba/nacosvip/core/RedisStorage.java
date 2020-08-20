package com.alibaba.nacosvip.core;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacosvip.util.ApplicationUtils;
import com.alibaba.nacosvip.util.Constants;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * <pre>
 * </pre>
 * RedisStorage
 * Date: 2020/7/4 0004
 * Time: 21:39
 *
 * @author tang.xiaosheng@qq.com
 */
public class RedisStorage extends AbstractStorage {

    private static RedisTemplate redisTemplate = ApplicationUtils.getBean("redisTemplate", RedisTemplate.class);

    @Override
    public Set<String> get() {
        return redisTemplate.opsForSet().members(Constants.CACHE_NACOS_ADDRESS_LIST);
    }

    @Override
    public void post(Set<String> clusterIps) {
        if (CollectionUtils.isNotEmpty(clusterIps)) {
            redisTemplate.opsForSet().add(Constants.CACHE_NACOS_ADDRESS_LIST, clusterIps.toArray());
        }
    }

    @Override
    public void delete(Set<String> clusterIps) {
        if (CollectionUtils.isNotEmpty(clusterIps)) {
            redisTemplate.opsForSet().remove(Constants.CACHE_NACOS_ADDRESS_LIST, clusterIps.toArray());
        }
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete(Constants.CACHE_NACOS_ADDRESS_LIST);
    }
}
