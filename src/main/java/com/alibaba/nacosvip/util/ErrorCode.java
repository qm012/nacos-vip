package com.alibaba.nacosvip.util;


public enum ErrorCode {
    /**
     * error code
     */
    S_SERVICE_UNAVAILABLE(500, "System error."),
    S_SUCCESS(200, "success"),
    B_PERMISSION_ERROR_MESSAGE(403, "This permission is not available. please contact the administrator for handing."),
    B_IP_FORMAT_ERROR_MESSAGE(400, "Data format error, error data: %s"),
    B_IPS_PARAM_NOT_NULL_MESSAGE(400, "Cluster list parameter cannot be empty."),
    B_IPS_PARAM_LIMIT_MESSAGE(400, "Now address number is [%s], Limit the number of cluster list parameters: [1-%s] ");


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
