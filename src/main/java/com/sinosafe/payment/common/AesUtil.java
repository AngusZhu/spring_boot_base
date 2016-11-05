package com.sinosafe.payment.common;


import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AesUtil {
    private static BASE64Encoder base64Encoder;
    private static BASE64Decoder base64Decoder;
    private static IvParameterSpec iv;
    private static String CipherType = "AES/CBC/PKCS5Padding"; //"算法/模式/补码方式


    //测试环境
    private static final String AES_KEY= FileUtil.getAESKey();//"0c26e8d0671d4a15";
    //生产
//    private static final String AES_KEY="e92511edc1634b36";
    private static final String CHARSET="UTF-8";


    static {
        base64Decoder = new BASE64Decoder();
        base64Encoder = new BASE64Encoder();
        iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
    }

    public static String defaultEncrypt(String sSrc) throws Exception {
        if(StringUtils.isBlank(sSrc)){
            return "";
        }
        //return URLEncoder.encode(encrypt, CHARSET);
        return AesUtil.Encrypt(sSrc, AES_KEY);
    }
    public static String defaultUrlEncrypt(String sSrc) throws Exception {
        String encrypt = AesUtil.Encrypt(sSrc, AES_KEY);
        return URLEncoder.encode(encrypt, CHARSET);

    }

    public static String defaultDecrypt(String sSrc) throws Exception {
       String decode = URLDecoder.decode(sSrc, CHARSET);
        return Decrypt(decode, AES_KEY);
    }

    // 加密
    public static String Encrypt(String sSrc,String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
//        判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes(CHARSET);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CipherType);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(CHARSET));

        return base64Encoder.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String Decrypt(String sSrc) throws Exception {
        if(StringUtils.isBlank(sSrc)){
            return "";
        }
       return AesUtil.Decrypt(sSrc,AES_KEY);
    }
    // 解密
    public static String Decrypt(String sSrc,String sKey) throws Exception {
    	/*sSrc = new String(sSrc.getBytes(),CHARSET);*/
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes(CHARSET);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(CipherType);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = base64Decoder.decodeBuffer(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return  new String(original,CHARSET);
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }



    
    public static void main(String[] args) throws Exception {

      //  String token ="8c00558923f95eeec7bb5462e0b9ac6a";
        System.out.println(AesUtil.defaultUrlEncrypt("01280302"));
        System.out.println(AesUtil.defaultUrlEncrypt("161000728373"));
        System.out.println(AesUtil.defaultUrlEncrypt("http://172.168.1.155:8888/shop/sycp/car/toubao.html?target=payback&plyNo=161000728373"));
        System.out.println(AesUtil.defaultUrlEncrypt("WX5300603602016001550,WX5300603802016001550"));



      //  System.out.println(AesUtil.defaultDecrypt(name));
        //System.out.println(AesUtil.defaultDecrypt(token));

    }
}