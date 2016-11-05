/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <doc>系统静态配置参数<doc>
 *
 * @author chensf
 * @version 1.0 2013-8-22 下午10:01:42
 * @since 1.0
 */

@Component
@ConfigurationProperties
public class Constants {

    private String hxQueryUrl;

    private String postPaymentURL;

    private String bankCode;

    private String insureId;

    private String midNo;

    private String tidNo;

    private String userId;

    private String password;

    private String payMentServiceURL;// 支付平台服务地址

    private String resultPageURL;// 同步显示支付结果页面ACTION

    private String receiveURL;// 同步显示支付接收参数页面

    private String serviceURL;// DNS的地址

    private String bankComm_path;// 交行配置文件地址

    private String bill99PFXPath;// 保险公司私钥地址

    private String bill99PFXKey;// 私钥密码

    private String bill99PFXKeyName;// 私钥别名

    private String bill99CertPath;// 快钱公钥

    // add by qgx 2016-01-13 上海快钱接入相关配置  Generate Constructor using Fields
    private String sh_bill99PFXPath;// 保险公司私钥地址（上海快钱）
    private String sh_bill99PFXKey;// 私钥密码（上海快钱）
    private String sh_bill99PFXKeyName;// 私钥别名（上海快钱）

    private String sh_bill99CertPath;// 快钱公钥（上海快钱）

    private String hxBackUrl;

    private String microPlatUrl;

    private String ydbjBackUrl;

    private String zjywptBackUrl;


    private String weixinBackUrl;

    private String mpformUrl;

    private String projectDomain;

    public String getHxQueryUrl() {
        return hxQueryUrl;
    }

    public void setHxQueryUrl(String hxQueryUrl) {
        this.hxQueryUrl = hxQueryUrl;
    }

    public String getPostPaymentURL() {
        return postPaymentURL;
    }

    public void setPostPaymentURL(String postPaymentURL) {
        this.postPaymentURL = postPaymentURL;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
    }

    public String getMidNo() {
        return midNo;
    }

    public void setMidNo(String midNo) {
        this.midNo = midNo;
    }

    public String getTidNo() {
        return tidNo;
    }

    public void setTidNo(String tidNo) {
        this.tidNo = tidNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayMentServiceURL() {
        return payMentServiceURL;
    }

    public void setPayMentServiceURL(String payMentServiceURL) {
        this.payMentServiceURL = payMentServiceURL;
    }

    public String getResultPageURL() {
        return resultPageURL;
    }

    public void setResultPageURL(String resultPageURL) {
        this.resultPageURL = resultPageURL;
    }

    public String getReceiveURL() {
        return receiveURL;
    }

    public void setReceiveURL(String receiveURL) {
        this.receiveURL = receiveURL;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getBankComm_path() {
        return bankComm_path;
    }

    public void setBankComm_path(String bankComm_path) {
        this.bankComm_path = bankComm_path;
    }

    public String getBill99PFXPath() {
        return bill99PFXPath;
    }

    public void setBill99PFXPath(String bill99PFXPath) {
        this.bill99PFXPath = bill99PFXPath;
    }

    public String getBill99PFXKey() {
        return bill99PFXKey;
    }

    public void setBill99PFXKey(String bill99PFXKey) {
        this.bill99PFXKey = bill99PFXKey;
    }

    public String getBill99PFXKeyName() {
        return bill99PFXKeyName;
    }

    public void setBill99PFXKeyName(String bill99PFXKeyName) {
        this.bill99PFXKeyName = bill99PFXKeyName;
    }

    public String getBill99CertPath() {
        return bill99CertPath;
    }

    public void setBill99CertPath(String bill99CertPath) {
        this.bill99CertPath = bill99CertPath;
    }

    public String getSh_bill99PFXPath() {
        return sh_bill99PFXPath;
    }

    public void setSh_bill99PFXPath(String sh_bill99PFXPath) {
        this.sh_bill99PFXPath = sh_bill99PFXPath;
    }

    public String getSh_bill99PFXKey() {
        return sh_bill99PFXKey;
    }

    public void setSh_bill99PFXKey(String sh_bill99PFXKey) {
        this.sh_bill99PFXKey = sh_bill99PFXKey;
    }

    public String getSh_bill99PFXKeyName() {
        return sh_bill99PFXKeyName;
    }

    public void setSh_bill99PFXKeyName(String sh_bill99PFXKeyName) {
        this.sh_bill99PFXKeyName = sh_bill99PFXKeyName;
    }

    public String getSh_bill99CertPath() {
        return sh_bill99CertPath;
    }

    public void setSh_bill99CertPath(String sh_bill99CertPath) {
        this.sh_bill99CertPath = sh_bill99CertPath;
    }

    public String getHxBackUrl() {
        return hxBackUrl;
    }

    public void setHxBackUrl(String hxBackUrl) {
        this.hxBackUrl = hxBackUrl;
    }

    public String getMicroPlatUrl() {
        return microPlatUrl;
    }

    public void setMicroPlatUrl(String microPlatUrl) {
        this.microPlatUrl = microPlatUrl;
    }

    public String getYdbjBackUrl() {
        return ydbjBackUrl;
    }

    public void setYdbjBackUrl(String ydbjBackUrl) {
        this.ydbjBackUrl = ydbjBackUrl;
    }

    public String getZjywptBackUrl() {
        return zjywptBackUrl;
    }

    public void setZjywptBackUrl(String zjywptBackUrl) {
        this.zjywptBackUrl = zjywptBackUrl;
    }

    public String getWeixinBackUrl() {
        return weixinBackUrl;
    }

    public void setWeixinBackUrl(String weixinBackUrl) {
        this.weixinBackUrl = weixinBackUrl;
    }

    public String getMpformUrl() {
        return mpformUrl;
    }

    public void setMpformUrl(String mpformUrl) {
        this.mpformUrl = mpformUrl;
    }

    public String getProjectDomain() {
        return projectDomain;
    }

    public void setProjectDomain(String projectDomain) {
        this.projectDomain = projectDomain;
    }
}
