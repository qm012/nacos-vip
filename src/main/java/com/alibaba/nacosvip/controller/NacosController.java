package com.alibaba.nacosvip.controller;

import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.alibaba.nacosvip.core.StorageFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 * NacosController
 * Date: 2020/6/2 0002
 * Time: 9:28
 *
 * @author tang.xiaosheng@qq.com
 */
@SuppressWarnings("unchecked")
@RestController
public class NacosController {

    /**
     * client get serverList
     *
     * @return server list string
     */
    @GetMapping("/nacos/serverlist")
    public String serverListByClient() {
        return StorageFactory.get();
    }

    /**
     * server get serverList
     *
     * @return RestResult
     */
    @GetMapping("/nacos/server/serverlist")
    public RestResult serverListByServer() {
        try {
            return RestResultUtils.success(StorageFactory.get());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultUtils.failed(e.getMessage());
        }
    }

    /**
     * add server list
     *
     * @param nacosVipCo client object
     * @return RestResult
     */
    @PostMapping("/nacos/serverlist")
    public RestResult post(@RequestBody NacosVipCO nacosVipCo) {
        return StorageFactory.post(nacosVipCo);
    }

    /**
     * delete an item in the server list
     *
     * @param nacosVipCo client object
     * @return RestResult
     */
    @DeleteMapping("/nacos/serverlist")
    public RestResult delete(@RequestBody NacosVipCO nacosVipCo) {
        return StorageFactory.delete(nacosVipCo);
    }


    /**
     * delete all server list
     *
     * @return RestResult
     */
    @DeleteMapping("/nacos/serverlist/all")
    public RestResult deleteAllData() {
        return StorageFactory.deleteAll();
    }
}
