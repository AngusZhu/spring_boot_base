package com.sinosafe.payment.service.impl;

import com.sinosafe.payment.dao.mapper.PrimarykeyMapper;
import com.sinosafe.payment.service.PrimarykeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhuhuanmin on 2016/4/21.
 */
@Service
public class PrimarykeyServiceImpl implements PrimarykeyService{

  //  @Autowired
    public  PrimarykeyMapper primarykeyMapper;


    public Long getSeqence(){
       return 1l;// primarykeyMapper.getSeqence();
    }


}
