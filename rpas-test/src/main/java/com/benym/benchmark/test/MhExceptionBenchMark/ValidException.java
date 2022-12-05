package com.benym.benchmark.test.MhExceptionBenchMark;


/**
 * 校验异常类，固定状态码，不打印堆栈信息
 *
 * @Time: 2022/11/11 20:36
 */
public class ValidException extends AbstractException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_VALID_ERRCODE = "test";

    public ValidException() {
        super(DEFAULT_VALID_ERRCODE);
    }

    public ValidException(String errMessage) {
        super(DEFAULT_VALID_ERRCODE, errMessage);
    }
}
