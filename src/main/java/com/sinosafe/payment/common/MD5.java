package com.sinosafe.payment.common;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5 {

    private static final Logger logger = LoggerFactory.getLogger(MD5.class);
    public final static String MD5_KEY= FileUtil.getMD5Key();//"1dd40319ec43f6b0b2e7a9053cfb5cf2"; //测试环境
//    public final static String MD5_KEY="350b0bd7c607191cbf3fe5f687ffe5cd";  //生产环境

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * J 转换byte到16进制
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * J 编码
	 * 
	 * @param origin
	 * @return
	 */

	// MessageDigest 为 JDK 提供的加密类
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes("UTF-8")));
		} catch (Exception ex) {
		}
		return resultString;
	}
	
	// MessageDigest 为 JDK 提供的加密类
	public static String MD5Encode(byte[] bytes) {
		String resultString = null;
		try {
//			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(bytes));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	
	public static String MD5Encode(String origin,String charsetName) {
		origin =origin.trim();
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes(charsetName)));
		} catch (Exception ex) {
		}
		return resultString;
	}

    public static String getToken(String... strings){
        String join = Joiner.on("|").join(strings);
        logger.info(join);
        return MD5.MD5Encode(join);
    }

    public static void main(String[] args) throws Exception {
        String[] a="".toString().split(",");
        System.out.println(a.length);
        System.out.println(MD5.getToken("sinosafe", "447","5000",FileUtil.getMD5Key(),"2016-10-29"));
//        System.out.println(AesUtil.defaultDecrypt("mWThnYTuPpGwZ2LHCBgOVGJJIVvyWzTsRBBWpCYoySQ="));
//        System.out.println(AesUtil.defaultDecrypt("CkQRTRTN6EPB2DJNkQO8oQ=="));
        System.out.println(MD5.getToken("Hx01|sinosafe|华安保险|59280205876953088|2016-11-01|12111477882435315|549502|54900|494602|127.0.0.1|老罗|6226090217436936|0|420881199110230458|15361611147|1dd40319ec43f6b0b2e7a9053cfb5cf2"));

    }



}
