package com.sinosafe.payment.service.common.impl;

import com.sinosafe.payment.dao.mapper.common.PrimarykeyMapper;
import com.sinosafe.payment.service.common.PrimarykeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
@Service("primarykeyService")
public class PrimarykeyServiceImpl implements PrimarykeyService{

    @Autowired
    public  PrimarykeyMapper primarykeyMapper;


    public Long getSeqence(){
       return  primarykeyMapper.getSeqence();
    }


}
