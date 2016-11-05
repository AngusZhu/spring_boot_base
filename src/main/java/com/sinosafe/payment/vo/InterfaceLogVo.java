package com.sinosafe.payment.vo;

import com.sinosafe.payment.constants.SystemCode;

import java.io.Serializable;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
public class InterfaceLogVo implements Serializable {

    private Long logId;
    private SystemCode systemCode;
    private String interfaceCode;
    private String orderNo;
    private String InParam;
    private String outParam;


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public SystemCode getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(SystemCode systemCode) {
        this.systemCode = systemCode;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInParam() {
        return InParam;
    }

    public void setInParam(String inParam) {
        InParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }


    @Override
    public String toString() {
        return "InterfaceLogVo{" +
                "logId=" + logId +
                ", systemCode=" + systemCode +
                ", interfaceCode='" + interfaceCode + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", InParam='" + InParam + '\'' +
                ", outParam='" + outParam + '\'' +
                '}';
    }
}
