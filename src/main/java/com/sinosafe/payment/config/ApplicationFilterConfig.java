package com.sinosafe.payment.config;

import com.alibaba.druid.support.http.WebStatFilter;
import com.sinosafe.payment.filter.CorsFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class ApplicationFilterConfig {

    @Autowired
    private Environment env;

    @Bean
    public FilterRegistrationBean encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        FilterRegistrationBean mappingEncodingFilter = new FilterRegistrationBean(encodingFilter);
        mappingEncodingFilter.setEnabled(true);
        mappingEncodingFilter.addUrlPatterns("/*");
        mappingEncodingFilter.setOrder(1);
        return mappingEncodingFilter;
    }

    /**sso start filter***/
    @Bean
    public FilterRegistrationBean casFilter(){
        Filter authFilter = new AuthenticationFilter();
        FilterRegistrationBean mappingAuth = new FilterRegistrationBean(authFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/web/*");
        Map<String,String> initMap=new ConcurrentHashMap<>();
        initMap.put("casServerLoginUrl",env.getProperty("sso.login.url"));
        initMap.put("serverName",env.getProperty("front.web.url"));
        mappingAuth.setInitParameters(initMap);
        mappingAuth.setUrlPatterns(urlPatterns);
        mappingAuth.setOrder(2);
        return mappingAuth;
    }

    @Bean
    public FilterRegistrationBean validateFilter(){
        Filter validateFilter = new Cas20ProxyReceivingTicketValidationFilter();
        FilterRegistrationBean mappingValidate = new FilterRegistrationBean(validateFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/web/*");
        Map<String,String> initMap=new ConcurrentHashMap<>();
        initMap.put("casServerUrlPrefix",env.getProperty("sso.login.url"));
        initMap.put("serverName",env.getProperty("front.web.url"));
        mappingValidate.setInitParameters(initMap);
        mappingValidate.setUrlPatterns(urlPatterns);
        mappingValidate.setOrder(3);
        return mappingValidate;
    }

    @Bean
    public FilterRegistrationBean wrapperFilter(){
        Filter wrapperFilter = new HttpServletRequestWrapperFilter();
        FilterRegistrationBean mappingWrapper = new FilterRegistrationBean(wrapperFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/web/*");
        mappingWrapper.setUrlPatterns(urlPatterns);
        mappingWrapper.setOrder(3);
        return mappingWrapper;
    }

    @Bean
    public FilterRegistrationBean assertFilter(){
        Filter assertFilter = new AssertionThreadLocalFilter();
        FilterRegistrationBean mappingAssert = new FilterRegistrationBean(assertFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/web/*");
        mappingAssert.setUrlPatterns(urlPatterns);
        mappingAssert.setOrder(3);
        return mappingAssert;
    }

    /**sso end filter***/

    @Bean
    public FilterRegistrationBean webStatFilter() {

        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean mappingDruid = new FilterRegistrationBean(webStatFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        Map<String,String> initMap=new ConcurrentHashMap<>();
        initMap.put("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        mappingDruid.setInitParameters(initMap);
        mappingDruid.setUrlPatterns(urlPatterns);
        mappingDruid.setOrder(3);
        return mappingDruid;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {

        Filter corsFilter = new CorsFilter();
        FilterRegistrationBean mappingDruid = new FilterRegistrationBean(corsFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        mappingDruid.setUrlPatterns(urlPatterns);
        mappingDruid.setOrder(4);
        return mappingDruid;
    }




}
