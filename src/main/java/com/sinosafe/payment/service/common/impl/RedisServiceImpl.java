//package com.sinosafe.payment.service.common.impl;
//
//
//import com.sinosafe.payment.service.common.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.stereotype.Service;
//
///**
// * Created with base.
// * User: anguszhu
// * Date: Apr,07 2016
// * Time: 4:32 PM
// * description:
// */
//@Service("redisService")
//public class RedisServiceImpl implements RedisService {
//
//    @Autowired
//    private JedisConnectionFactory jedisConnectionFactory;
//
//    @Autowired
//    private Environment env;
//
//    /**
//     * 获取Jedis实例
//     *
//     * @return
//     */
//    public synchronized RedisConnection getConnection() {
//        try {
//            if (jedisConnectionFactory != null) {
//                RedisConnection redisConnection = jedisConnectionFactory.getConnection();
//                return redisConnection;
//            }
//            return null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 释放jedis资源
//     *
//     * @param
//     */
//    public void returnResource(final RedisConnection redisConnection) {
//        if (redisConnection != null) {
//            redisConnection.close();
//        }
//    }
//
//
//    /**
//     * 设置常量配置到缓存
//     */
//    public void setEnvProperties() {
//
//
//    }
//
//
//}
