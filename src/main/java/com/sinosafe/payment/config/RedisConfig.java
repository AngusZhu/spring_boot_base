/*
package com.sinosafe.payment.config;


import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

*/
/**
 * Created with base.
 * User: anguszhu
 * Date: Apr,14 2016
 * Time: 4:16 PM
 * description:
 *//*

@Configuration
@EnableRedisHttpSession
public class RedisConfig {



    @Autowired
    private Environment env2;

    public JedisPoolConfig jpc=null;


    @Bean
    public JedisPoolConfig getJedisPoolConfig(){
        if(jpc == null) {
            jpc=new JedisPoolConfig();
            jpc.setMaxIdle(Integer.valueOf(env2.getProperty("spring.redis.pool.maxIdle")));
            jpc.setMaxTotal(Integer.valueOf(env2.getProperty("spring.redis.pool.maxTotal")));
            jpc.setMinIdle(Integer.valueOf(env2.getProperty("spring.redis.pool.minIdle")));
            jpc.setMaxWaitMillis(Integer.valueOf(env2.getProperty("spring.redis.pool.maxWaitTime")));
        }
        return jpc;
    }
    */
/**
     *
     * @return
     *//*

    @Bean
    public  JedisConnectionFactory getConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(getSentinelConfiguration(),getJedisPoolConfig());
        return connectionFactory;
    }


    public  RedisSentinelConfiguration getSentinelConfiguration() {
        String nodesStr = env2.getProperty("spring.redis.sentinel.nodes");
        if (StringUtils.isNotEmpty(nodesStr)) {
            String[] nodesArr = nodesStr.split(",");
            Set<String> set = new HashSet(Arrays.asList(nodesArr));
            return new RedisSentinelConfiguration(env2.getProperty("spring.redis.sentinel.masterName"), set);
        }
        return null;
    }


}*/
