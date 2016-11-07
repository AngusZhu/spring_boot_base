package com.sinosafe.payment.http;


import com.sinosafe.payment.common.http.SendDataByHTTPS;

/**
 * 
 * @author admin
 * @date 2015年11月1日 下午2:23:18 
 * @version 1.0 
 */
public class SendDataToolTest {
	
	
	public static void main(String[] args) throws Exception {
		String url="https://www.facebook.com/";
		System.out.println(new SendDataByHTTPS().send("", url, null));
	}
	
}