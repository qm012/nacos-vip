package com.alibaba.nacosvip.util;

import com.alibaba.nacos.common.model.RestResult;
import com.alibaba.nacos.common.model.RestResultUtils;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * </pre>
 * RequestParamUtil
 * Date: 2020/6/2 0002
 * Time: 9:38
 *
 * @author tang.xiaosheng@qq.com
 */
public class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * get HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * check access token
     *
     * @return response
     */
    public static RestResult checkAccessToken() {
        String accessToken = ApplicationUtils.getProperty(Constants.ACCESS_TOKEN);
        if (accessToken != null && accessToken.trim().length() > 0
                && !accessToken.equals(getRequest().getHeader("Access-Token"))) {
            LOGGER.info(ErrorCode.B_PERMISSION_ERROR_MESSAGE.getMessage());

            return RestResultUtils.failedWithMsg(ErrorCode.B_PERMISSION_ERROR_MESSAGE.getCode(), ErrorCode.B_PERMISSION_ERROR_MESSAGE.getMessage());
        }
        return RestResultUtils.success();
    }

    /**
     * authentication authority and ip format
     *
     * @return response
     */
    public static RestResult checkIpsAndAccessToken(Set<String> clusterIps) {
        // check access token
        RestResult tokenResult = checkAccessToken();
        if (!tokenResult.ok()) {
            return tokenResult;
        }

        // check request data is not null
        if (CollectionUtils.isEmpty(clusterIps)) {

            return RestResultUtils.failedWithMsg(ErrorCode.B_IPS_PARAM_NOT_NULL_MESSAGE.getCode(), ErrorCode.B_IPS_PARAM_NOT_NULL_MESSAGE.getMessage());

        }
        //check limit data num
        String clusterListSize = ApplicationUtils.getProperty(Constants.CLUSTER_LIST_SIZE);
        if (clusterListSize != null && clusterIps.size() > Long.parseLong(clusterListSize)) {
            String error = String.format(ErrorCode.B_IPS_PARAM_LIMIT_MESSAGE.getMessage(), clusterIps.size(), clusterListSize);
            LOGGER.info(error);

            return RestResultUtils.failedWithMsg(ErrorCode.B_IPS_PARAM_LIMIT_MESSAGE.getCode(), error);
        }

        Set<String> errorIps = clusterIps.parallelStream().filter(ip -> !ValidationUtil.isCorrectIpAddress(ip)).collect(Collectors.toSet());
        // check data format
        if (!errorIps.isEmpty()) {
            String error = String.format(ErrorCode.B_IP_FORMAT_ERROR_MESSAGE.getMessage(), errorIps);
            LOGGER.error(error);

            return RestResultUtils.failedWithMsg(ErrorCode.B_IP_FORMAT_ERROR_MESSAGE.getCode(), error);
        }
        return RestResultUtils.success();
    }
}