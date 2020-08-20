package com.alibaba.nacosvip.core;

import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacosvip.controller.NacosVipCO;
import com.alibaba.nacosvip.util.ApplicationUtils;
import com.alibaba.nacosvip.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * <pre>
 * </pre>
 * StorageFactory
 * Date: 2020/7/2 0002
 * Time: 17:35
 *
 * @author tang.xiaosheng@qq.com
 */
public final class StorageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageFactory.class);
    private static Storage storage;

    static {
        storage = StorageModel.getStorage(getStorageModel());
    }

    public static String get() {
        Set<String> nacosAddressList = storage.get();
        String serverList = null;
        if (CollectionUtils.isNotEmpty(nacosAddressList)) {
            StringBuilder ips = new StringBuilder();
            nacosAddressList.forEach(ip -> ips.append(ip).append("\n"));
            serverList = ips.toString();
        }
        LOGGER.info("Nacos-vip server list: {}", serverList);
        return serverList;
    }

    public static RestResult post(NacosVipCO nacosVipCo) {
        try {

            RestResult restResult = RequestUtil.checkIpsAndAccessToken(nacosVipCo.getClusterIps());
            if (!restResult.ok()) {
                return restResult;
            }

            storage.post(nacosVipCo.getClusterIps());

            LOGGER.info("Add server list data: {}", nacosVipCo.getClusterIps().toString());
            return restResult;
        } catch (Exception e) {
            LOGGER.error("Add data error message: {}", e.getMessage());
            e.printStackTrace();
            return RestResultUtils.failed(e.getMessage());
        }

    }

    public static RestResult delete(NacosVipCO nacosVipCo) {

        try {

            LOGGER.info("Delete server list data: {}", nacosVipCo.getClusterIps());

            RestResult restResult = RequestUtil.checkIpsAndAccessToken(nacosVipCo.getClusterIps());
            if (!restResult.ok()) {
                return restResult;
            }

            storage.delete(nacosVipCo.getClusterIps());

            LOGGER.info("Delete server list success.");
            return restResult;
        } catch (Exception e) {
            LOGGER.error("Delete data error message: {}", e.getMessage());
            e.printStackTrace();
            return RestResultUtils.failed(e.getMessage());
        }
    }


    public static RestResult deleteAll() {
        try {

            RestResult tokenResult = RequestUtil.checkAccessToken();
            if (tokenResult.ok()) {
                storage.deleteAll();
            }
            LOGGER.info("Clear all server list success.");
            return tokenResult;
        } catch (Exception e) {
            LOGGER.error("Clear all data error message: {}", e.getMessage());
            e.printStackTrace();
            return RestResultUtils.failed(e.getMessage());
        }
    }


    /**
     * get storage model
     */
    public static StorageModel getStorageModel() {
        if (ApplicationUtils.getStandaloneMode()) {
            if (ApplicationUtils.isSetRedis()) {
                return StorageModel.REDIS;
            }
            return StorageModel.CACHE;

        } else {
            if (ApplicationUtils.isSetRedis()) {
                return StorageModel.REDIS;
            }
            return StorageModel.EMBEDDED;
        }
    }

    enum StorageModel {
        /**
         * Storage model key
         */
        CACHE,
        REDIS,
        EMBEDDED;

        public static Storage getStorage(StorageModel model) {
            Storage storage = null;
            switch (model) {
                case REDIS:
                    storage = new RedisStorage();
                    break;
                case CACHE:
                    storage = new CacheStorage();
                    break;
                case EMBEDDED:
                    storage = new EmbeddedStorage();
                    break;
                default:
                    LOGGER.error("Please select storage model.");
                    break;
            }
            return storage;
        }
    }
}
