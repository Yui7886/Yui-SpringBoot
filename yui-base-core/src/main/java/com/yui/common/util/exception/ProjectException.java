package com.yui.common.util.exception;

/**
 * 项目自定义异常
 *
 * @author yui
 */
public class ProjectException extends RuntimeException {

    public ProjectException() {
        super();
    }

    public ProjectException(Exception e) {
        super(e);
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

}
