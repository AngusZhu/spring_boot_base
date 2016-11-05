package com.sinosafe.payment.common;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
public enum ReturnCode {


    // 利用构造函数传参
    RETURN_CODE("returnCode"),ERROR_DESC("errorMsg"),BUSINESS_DTO("businessDto");

    // 定义私有变量
    private String code;

    // 构造函数，枚举类型只能为私有
    private ReturnCode(String code) {

        this.code = code;

    }

    @Override
    public String toString() {

        return String.valueOf(this.code);

    }

}
