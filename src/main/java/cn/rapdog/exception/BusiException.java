package cn.rapdog.exception;

import java.io.Serializable;

public class BusiException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    String expCode = null;

    /**
     * 异常信息
     */
    String expMessage = null;

    public BusiException() {

    }

    /**
     * 构造器.<br>
     * 
     * @param message 异常信息
     * @ServiceMethod
     */
    public BusiException(String message) {
        super(message);
    }

    /**
     * 构造器.<br>
     * 
     * @param oriEx 异常对象
     * @ServiceMethod
     */
    public BusiException(Exception oriEx) {
        super(oriEx);
    }

    public BusiException(Throwable oriEx) {
        super(oriEx);
    }

    /**
     * 构造器.<br>
     * 
     * @param message
     * @param oriEx
     * @ServiceMethod
     */
    public BusiException(String message, Exception oriEx) {
        super(message, oriEx);
    }

    public BusiException(String message, Throwable oriEx) {
        super(message, oriEx);
    }

    public BusiException(String expCode, String message, Throwable oriEx) {
        super(expCode + ":" + message, oriEx);
        this.expCode = expCode;
    }

    public String getErrorCode() {
        return expCode;
    }

    public void setErrorCode(String expCode) {
        this.expCode = expCode;
    }

    public String getErrorMessage() {
        return expMessage;
    }

    public void setErrorMessage(String expMessage) {
        this.expMessage = expMessage;
    }

}
