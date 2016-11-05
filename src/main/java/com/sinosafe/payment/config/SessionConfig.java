package com.sinosafe.payment.config;


import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with base.
 * User: anguszhu
 * Date: Apr,14 2016
 * Time: 4:16 PM
 * description:
 */
@EnableRedisHttpSession
public class SessionConfig {
    @Autowired
    private Environment env;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connection = new JedisConnectionFactory(getSentinelConfiguration());
        return connection;
    }


    public RedisSentinelConfiguration getSentinelConfiguration() {
        String nodesStr = env.getProperty("spring.redis.sentinel.nodes");
        if (StringUtils.isNotEmpty(nodesStr)) {
            String[] nodesArr = nodesStr.split(",");
            Set<String> set = new HashSet(Arrays.asList(nodesArr));
            return new RedisSentinelConfiguration(env.getProperty("spring.redis.sentinel.masterName"), set);
        }
        return null;
    }
}