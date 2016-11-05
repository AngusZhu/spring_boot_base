package com.sinosafe.payment.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhuhuanmin on 2016-6-12.
 */
public class FileUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static Properties prop= new Properties();
    static {
//         env = System.getProperty("env");
        InputStream in = null;
        try {
            in =FileUtil.class.getClassLoader().getResourceAsStream("keys.properties");
            prop.load(in);
        } catch (Exception e) {
            //生产配置
            logger.error("获取秘钥配置文件异常,将使用生产配置",e.getMessage());
            prop.setProperty("MD5_KEY","1dd40319ec43f6b0b2e7a9053cfb5cf2");
            prop.setProperty("AES_KEY","0c26e8d0671d4a15");
//            prop.setProperty("MD5_KEY","350b0bd7c607191cbf3fe5f687ffe5cd");
//            prop.setProperty("AES_KEY","e92511edc1634b36");
        }
    }

    public static String getMD5Key() {
        return prop.getProperty("MD5_KEY");
    }

    public static String getAESKey(){
        return prop.getProperty("AES_KEY");
    }
}
