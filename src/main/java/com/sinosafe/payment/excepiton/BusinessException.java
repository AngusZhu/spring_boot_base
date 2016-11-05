package com.sinosafe.payment.excepiton;

/**
 * Created with base.
 * User: anguszhu
 * Date: Apr,06 2016
 * Time: 8:00 PM
 * description:
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
