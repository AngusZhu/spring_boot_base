package com.sinosafe.payment.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.sinosafe.payment.constants.SystemCode;
import com.sinosafe.payment.dao.mapper.LogMapper;
import com.sinosafe.payment.dao.mapper.LogMapper;
import com.sinosafe.payment.vo.InterfaceLogVo;
import com.sinosafe.payment.service.LogService;
import com.sinosafe.payment.vo.InterfaceLogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    private final static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    LogMapper logMapper;


    @Transactional
    @Override
    public void saveExceptionLog(String method, Exception e) {
        try {
           // logMapper.saveExceptionLog(method, StringUtils.subString(e.toString(), "0", "500"), e.toString());
        } catch (RuntimeException re) {
            logger.error("saveExceptionLog", re.toString());
            re.printStackTrace();
        }
    }

    /**
     *   报存接口调用入参
     * @param logId  接口流水主键
     * @param systemCode  调用、被调用系统标识
     * @param interfaceCode 接口名称
     * @param orderNo   订单号或关键信息
     * @param inParam  入参
     */
    @Transactional
    @Override
    public void saveRequestLog(Long logId, SystemCode systemCode, String interfaceCode, String orderNo, String inParam) {
        saveRequestLog( logId,  systemCode.toString(),  interfaceCode,  orderNo,  inParam);
    }

    @Override
    public void saveRequestLog(Long logId, String systemCode, String interfaceCode, String orderNo, String inParam) {
        try {
           logMapper.saveRequestLog(logId, systemCode, interfaceCode,orderNo,inParam);
        } catch (RuntimeException re) {
            logger.error("saveRequestLog",re.toString());
            re.printStackTrace();
        }
    }

    /**
     *   报存接口返回信息
     * @param logId     接口流水主键 与接口入参Id 一致
     * @param systemCode 调用、被调用系统标识
     * @param interfaceCode  接口名称
     * @param orderNo   订单号或关键信息
     * @param outParam  接口返回参数
     */
    @Transactional
    @Override
    public void updateResponseLog(Long logId, SystemCode systemCode, String interfaceCode, String orderNo, String outParam) {
        updateResponseLog(logId, systemCode.toString(), interfaceCode, orderNo, outParam);
    }

    @Override
    public void updateResponseLog(Long logId, String systemCode, String interfaceCode, String orderNo, String outParam) {
        try {
          //  logMapper.updateResponseLog(logId, systemCode, interfaceCode, orderNo, outParam);
        } catch (RuntimeException re) {
            logger.error("saveResponseLog", re.toString());
            re.printStackTrace();
        }
    }

    /**
     *  同saveReqeustLog
     * @param requestDto
     */
    @Override
    public void saveReqeustLog(InterfaceLogVo requestDto) {
        saveRequestLog(requestDto.getLogId(),requestDto.getSystemCode(),requestDto.getInterfaceCode(),requestDto.getOrderNo(),requestDto.getInParam());
    }

    /**
     * 同updateResponseLog
     * @param requestDto
     */
    @Override
    public void updateResponseLog(InterfaceLogVo requestDto) {
        updateResponseLog(requestDto.getLogId(),requestDto.getSystemCode(),requestDto.getInterfaceCode(),requestDto.getOrderNo(),requestDto.getOutParam());
    }


}
