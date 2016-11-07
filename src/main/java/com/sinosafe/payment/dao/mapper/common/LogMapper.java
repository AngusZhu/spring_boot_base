package com.sinosafe.payment.dao.mapper.common;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */

public interface LogMapper {

    void saveExceptionLog(@Param("method") String method, @Param("description") String description, @Param("stackTrace") String stackTrace) ;

    void saveRequestLog(@Param("logId") Long logId, @Param("systemCode") String systemCode, @Param("interfaceCode") String interfaceCode, @Param("orderNo") String orderNo, @Param("inParam") String inParam) ;

    void updateResponseLog(@Param("logId") Long logId, @Param("systemCode") String systemCode, @Param("interfaceCode") String interfaceCode, @Param("orderNo") String orderNo, @Param("outParam") String outParam);


}
