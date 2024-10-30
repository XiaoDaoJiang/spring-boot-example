package com.xiaodao.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 服务器异常 Exception
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class ServerException extends RuntimeException {

    /**
     * 全局错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServerException() {
    }

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServerException(String message) {
        this.message = message;
        this.code = 500;
    }


    public ServerException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
