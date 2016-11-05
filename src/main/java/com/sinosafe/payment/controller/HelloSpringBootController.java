package com.sinosafe.payment.controller;

import com.sinosafe.payment.service.LogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Angus on Nov 8,2015.
 */
@RestController
@RequestMapping("/")
public class HelloSpringBootController {

    private final static Log log = LogFactory.getLog(HelloSpringBootController.class);
    @Autowired
    LogService logService;

    @RequestMapping("/")
    public String sayHello(){
        logService.saveRequestLog(123l,"WEIXIN","sayHello","1234556","wahaha");
        return "Hello payment! --:";
    }

}
