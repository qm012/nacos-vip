package com.alibaba.nacosvip;

import com.alibaba.nacosvip.core.StorageFactory;
import com.alibaba.nacosvip.util.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <pre>
 * </pre>
 * MemberApplication
 * Date: 2019/7/23 0023
 * Time: 16:12
 *
 * @author tang.xiaosheng@qq.com
 */
@SpringBootApplication
public class NacosVipApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosVipApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(NacosVipApplication.class);
        springApplication.addInitializers(new ApplicationUtils());
        springApplication.run(args);
        LOGGER.info("Nacos-vip started successfully in {} mode. use {} storage", ApplicationUtils.getStandaloneMode() ? "standalone" : "cluster",
                StorageFactory.getStorageModel());
    }
}
