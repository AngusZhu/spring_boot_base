package com.sinosafe.payment.constants;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
public enum SystemCode {


    // 利用构造函数传参
    B2C("B2C"), YDZY("YDZY"), WEIXIN("WEIXIN"),FUIOU("FUIOU"),SINOSAFE("com/sinosafe");

    // 定义私有变量
    private String code;

    // 构造函数，枚举类型只能为私有
    private SystemCode(String code) {

        this.code = code;

    }

    @Override
    public String toString() {

        return String.valueOf(this.code);

    }

}
