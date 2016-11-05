package com.sinosafe.payment.constants;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
public enum PointsType  {


    // 利用构造函数传参
    LOGIN("LOGIN"), ORDER("ORDER"), DOWNLOAD_APP("DOWNLOAD_APP"),REGIST("REGIST");

    // 定义私有变量
    private String code;

    // 构造函数，枚举类型只能为私有
    private PointsType(String code) {

        this.code = code;

    }

    @Override
    public String toString() {

        return String.valueOf(this.code);

    }

}
