package com.sinosafe.payment.interceptor;


import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import com.sinosafe.payment.config.SessionConfig;

/**
 * Created with base.
 * User: anguszhu
 * Date: Apr,09 2016
 * Time: 8:28 AM
 * description:
 */
public class SessionInitializer  extends AbstractHttpSessionApplicationInitializer {

    public SessionInitializer()
    {
        super(SessionConfig.class);
    }
}