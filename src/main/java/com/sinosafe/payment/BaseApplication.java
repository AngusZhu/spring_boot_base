package com.sinosafe.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Angus on Nov 8,2015.
 */

/**
 * The @SpringBootApplication annotation
 * is equivalent to using @Configuration, @EnableAutoConfiguration and @ComponentScan
 */
@SpringBootApplication
@PropertySource(value = {"classpath:/application.properties", "classpath:/datasource.properties"})
@ImportResource({"classpath:dubbo-provider.xml"})
public class BaseApplication {

    public static void main(String[] args) {
       /* SpringApplication app = new SpringApplication(BaseApplication.class);
        app.setShowBanner(false);
        app.run(args);*/

        SpringApplication.run(BaseApplication.class, args);
    }

}
