package com.tree.backendcommon.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),

    WRITE_OFF_ERROR(40200, "账号已被注销，禁止登录，请重新注册账号登录"),
    BAN_ERROR(40200, "此账号已被封号，禁止登录，请联系管理员解决"),
    /**
     * 40001 数据为空
     */
    NULL_ERROR(40001, "请求数据为空"),

    TOO_MANY_REQUEST(42900, "请求过于频繁"),
    API_REQUEST_ERROR(50010, "接口调用错误"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
