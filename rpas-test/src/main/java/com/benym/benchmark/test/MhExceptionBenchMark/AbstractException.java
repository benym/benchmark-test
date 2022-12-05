package com.benym.benchmark.test.MhExceptionBenchMark;


/**
 * 定义抽象异常类
 * This class is empowered by com.alibaba.cola
 *
 * @Time : 2022/7/7 22:20
 */
public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errCode;

    private String errMessage;

    private Throwable throwable;

    public AbstractException(String errMessage) {
        super(errMessage);
    }

    public AbstractException(String errMessage, Throwable throwable) {
        super(errMessage, throwable);
    }


    public AbstractException(String errCode, String errMessage) {
        this(errCode, errMessage, null);
    }

    public AbstractException(String errCode, String errMessage, Throwable throwable) {
        super(errMessage);
        this.setErrCode(errCode);
        this.setErrMessage(errMessage);
        this.setThrowable(throwable);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
