/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * <doc>支付网关核心工具类<doc>
 * @author chensf
 * @version 1.0 2013-8-18 上午10:00:36
 * @since 1.0
 * 
 */

public class GateWayCore {
	private static final Logger logger = Logger.getLogger(GateWayCore.class);
	
	/**
	 * 获得收款申请请求后进行相应的验证
	 * @param params
	 * @return
	 */
	public static boolean verify(Map<String, String> params, String key,String input_charset) {
		boolean verifyResult = false;
		String mysign = getMysign(params, key, input_charset);
		if (params.get("sign") != null) {
			if (params.get("sign").equals(mysign)) {
				verifyResult = true;
			}
		}
		return verifyResult;
	}
	
	

	

    /**
     * 对收款请求过来的参数进行验证(参数为请求对应的所有参数，此处会过虑sign及sign_type两个参数)
     * @param Params
     * @param key
     * @param input_charset
     * @return
     */
	public static String getMysign(Map<String, String> Params,String key,String input_charset) {
		Map<String, String> sParaNew = paraFilter(Params);//过虑参数空值
		//System.out.println("2==要加密串:"+sParaNew);
		String mysign =buildMysign(sParaNew,key,input_charset);//对输入的参数进行签名
		//System.out.println("2==加密后:"+sParaNew);
		return mysign;
	}
	
	
	 /**
    * 过虑参数空值,客户请求支付送过来的参数校验时也不应该把sign,sign_type和_input_charset放置在校验的参数里,如果支付方式为PAYSTATION则表示不传收单行时这个参数不是前置传入的，前端签名时不包含此值故这里不应该进行相应的加密
    * @param sArray
    * @return
    */
   @SuppressWarnings("unchecked")
	private static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map result = new HashMap();
		if ((sArray == null) || (sArray.size() <= 0)) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = (String) sArray.get(key);
			if ("null".equals(value)||(value == null) || "PAYSTATION".equals(value) || ("".equals(value)) || (key.equalsIgnoreCase("sign"))  ||(key.equalsIgnoreCase("_input_charset"))|| (key.equalsIgnoreCase("sign_type"))||key.equalsIgnoreCase("password") || key.equalsIgnoreCase("desc_BankCode")|| key.equalsIgnoreCase("inpay_bankCode")|| key.equalsIgnoreCase("inpay_order_bankCode")|| (key.equalsIgnoreCase("PayChannel")) ) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
	

	/**
	 * 对支付宝支付成功返回的输入的参数进行签名
     * @param key
     * @param input_charset：此用作输入Md5加密的入参
     * @return
	 */
	private static String buildMysign(Map<String, String> sArray,String key,String input_charset) {
		String prestr = createLinkString(sArray);//将请求参数Map构造成字符串
		if (logger.isDebugEnabled()) {
			logger.debug("待收款的签名请求参数值(已排序及过虑空值)["+prestr+"]");
			logger.debug("待收款的签名请求参数值(key值)["+key+"]");
		}
		prestr = prestr + key;
		System.out.println("prestr:"+prestr);
		String mysign =MD5.MD5Encode(prestr, input_charset);
		logger.debug("请求参数加密后密文(集中代收付)["+mysign+"]");
		return mysign;
	}
	
	
	/**
	 * 将Map参数构建成=?&连接的字符串
	 * @param params
	 * @return
	 */
    public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			if (i == keys.size() - 1){
				prestr = prestr + key + "=" + value;
			}else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

    public static Random ran = new Random();// 随机数类
    public static String create(int length) {
        char[] num = new char[length];// 生成一个1000位的char数组
        int temp;// 存放当前随机数
        char cur;// 存放当前字符
        for (int i = 0; i < num.length; i++) {
            temp = ran.nextInt(10);// 生成一个0-9的随机数
            cur = (char) ('0' + temp);// 转化成char型的数字
            num[i] = cur;// 放到数组的当前位
        }
        return new String(num);// 返回这个随机数(用字符串形式)
    }
}
