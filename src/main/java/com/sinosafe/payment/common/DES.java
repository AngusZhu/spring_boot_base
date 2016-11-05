/*
 * Created on 2009-3-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sinosafe.payment.common;


public class DES {
    // 声明常量字节数组
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54,
            46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33,
            25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21,
            13, 5, 63, 55, 47, 39, 31, 23, 15, 7
        }; // 64
    private static final int[] IP_1 = {
            40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6,
            46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12,
            52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18,
            58, 26, 33, 1, 41, 9, 49, 17, 57, 25
        }; // 64
    private static final int[] PC_1 = {
            57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51,
            43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7,
            62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20,
            12, 4
        }; // 56
    private static final int[] PC_2 = {
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16,
            7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44,
            49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
        }; // 48
    private static final int[] E = {
            32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13,
            14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24,
            25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
        }; // 48
    private static final int[] P = {
            16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8,
            24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
        }; // 32
    private static final int[][][] S_Box = {
            {
                { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
            },
            { // S_Box[1]
                { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
            },
            { // S_Box[2]
                { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
            },
            { // S_Box[3]
                { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
            },
            { // S_Box[4]
                { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
            },
            { // S_Box[5]
                { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
            },
            { // S_Box[6]
                { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
            },
            { // S_Box[7]
                { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
            } // S_Box[8]
        };
    private static final int[] LeftMove = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
        }; // 左移位置列表

    private byte[] UnitDes(byte[] des_key, byte[] des_data, int flag) {
        // 检测输入参数格式是否正确，错误直接返回空值（null）
        if ((des_key.length != 8) || (des_data.length != 8) ||
                ((flag != 1) && (flag != 0))) {
            throw new RuntimeException("Data Format Error !");
        }
        int flags = flag;
        // 二进制加密密钥
        int[] keydata = new int[64];
        // 二进制加密数据
        int[] encryptdata = new int[64];
        // 加密操作完成后的字节数组
        byte[] EncryptCode = new byte[8];
        // 密钥初试化成二维数组
        int[][] KeyArray = new int[16][48];
        // 将密钥字节数组转换成二进制字节数组
        keydata = ReadDataToBirnaryIntArray(des_key);
        // 将加密数据字节数组转换成二进制字节数组
        encryptdata = ReadDataToBirnaryIntArray(des_data);
        // 初试化密钥为二维密钥数组
        KeyInitialize(keydata, KeyArray);
        // 执行加密解密操作
        EncryptCode = Encrypt(encryptdata, flags, KeyArray);
        return EncryptCode;
    }

    // 初试化密钥数组
    private void KeyInitialize(int[] key, int[][] keyarray) {
        int i;
        int j;
        int[] K0 = new int[56];
        // 特别注意：xxx[IP[i]-1]等类似变换
        for (i = 0; i < 56; i++) {
            K0[i] = key[PC_1[i] - 1]; // 密钥进行PC-1变换
        }
        for (i = 0; i < 16; i++) {
            LeftBitMove(K0, LeftMove[i]);
            // 特别注意：xxx[IP[i]-1]等类似变换
            for (j = 0; j < 48; j++) {
                keyarray[i][j] = K0[PC_2[j] - 1]; // 生成子密钥keyarray[i][j]
            }
        }
    }

    // 执行加密解密操作
    private byte[] Encrypt(int[] timeData, int flag, int[][] keyarray) {
        int i;
        byte[] encrypt = new byte[8];
        int flags = flag;
        int[] M = new int[64];
        int[] MIP_1 = new int[64];
        // 特别注意：xxx[IP[i]-1]等类似变换
        for (i = 0; i < 64; i++) {
            M[i] = timeData[IP[i] - 1]; // 明文IP变换
        }
        if (flags == 1) { // 加密
            for (i = 0; i < 16; i++) {
                LoopF(M, i, flags, keyarray);
            }
        } else if (flags == 0) { // 解密
            for (i = 15; i > -1; i--) {
                LoopF(M, i, flags, keyarray);
            }
        }
        for (i = 0; i < 64; i++) {
            MIP_1[i] = M[IP_1[i] - 1]; // 进行IP-1运算
        }
        GetEncryptResultOfByteArray(MIP_1, encrypt);
        // 返回加密数据
        return encrypt;
    }

    private int[] ReadDataToBirnaryIntArray(byte[] intdata) {
        int i;
        int j;
        // 将数据转换为二进制数，存储到数组
        int[] IntDa = new int[8];
        for (i = 0; i < 8; i++) {
            IntDa[i] = intdata[i];
            if (IntDa[i] < 0) {
                IntDa[i] += 256;
                IntDa[i] %= 256;
            }
        }
        int[] IntVa = new int[64];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                IntVa[((i * 8) + 7) - j] = IntDa[i] % 2;
                IntDa[i] = IntDa[i] / 2;
            }
        }
        return IntVa;
    }

    private void LeftBitMove(int[] k, int offset) {
        int i;
        // 循环移位操作函数
        int[] c0 = new int[28];
        int[] d0 = new int[28];
        int[] c1 = new int[28];
        int[] d1 = new int[28];
        for (i = 0; i < 28; i++) {
            c0[i] = k[i];
            d0[i] = k[i + 28];
        }
        if (offset == 1) {
            for (i = 0; i < 27; i++) { // 循环左移一位
                c1[i] = c0[i + 1];
                d1[i] = d0[i + 1];
            }
            c1[27] = c0[0];
            d1[27] = d0[0];
        } else if (offset == 2) {
            for (i = 0; i < 26; i++) { // 循环左移两位
                c1[i] = c0[i + 2];
                d1[i] = d0[i + 2];
            }
            c1[26] = c0[0];
            d1[26] = d0[0];
            c1[27] = c0[1];
            d1[27] = d0[1];
        }
        for (i = 0; i < 28; i++) {
            k[i] = c1[i];
            k[i + 28] = d1[i];
        }
    }

    private void LoopF(int[] M, int times, int flag, int[][] keyarray) {
        int i;
        int j;
        int[] L0 = new int[32];
        int[] R0 = new int[32];
        int[] L1 = new int[32];
        int[] R1 = new int[32];
        int[] RE = new int[48];
        int[][] S = new int[8][6];
        int[] sBoxData = new int[8];
        int[] sValue = new int[32];
        int[] RP = new int[32];
        for (i = 0; i < 32; i++) {
            L0[i] = M[i]; // 明文左侧的初始化
            R0[i] = M[i + 32]; // 明文右侧的初始化
        }
        for (i = 0; i < 48; i++) {
            RE[i] = R0[E[i] - 1]; // 经过E变换扩充，由32位变为48位
            RE[i] = RE[i] + keyarray[times][i]; // 与KeyArray[times][i]按位作不进位加法运算
            if (RE[i] == 2) {
                RE[i] = 0;
            }
        }
        for (i = 0; i < 8; i++) { // 48位分成8组
            for (j = 0; j < 6; j++) {
                S[i][j] = RE[(i * 6) + j];
            }
            // 下面经过S盒，得到8个数
            sBoxData[i] = S_Box[i][(S[i][0] << 1) + S[i][5]][(S[i][1] << 3) +
                (S[i][2] << 2) + (S[i][3] << 1) + S[i][4]];
            // 8个数变换输出二进制
            for (j = 0; j < 4; j++) {
                sValue[((i * 4) + 3) - j] = sBoxData[i] % 2;
                sBoxData[i] = sBoxData[i] / 2;
            }
        }
        for (i = 0; i < 32; i++) {
            RP[i] = sValue[P[i] - 1]; // 经过P变换
            L1[i] = R0[i]; // 右边移到左边
            R1[i] = L0[i] + RP[i];
            if (R1[i] == 2) {
                R1[i] = 0;
            }
            // 重新合成M，返回数组M
            // 最后一次变换时，左右不进行互换。此处采用两次变换实现不变
            if (((flag == 0) && (times == 0)) ||
                    ((flag == 1) && (times == 15))) {
                M[i] = R1[i];
                M[i + 32] = L1[i];
            } else {
                M[i] = L1[i];
                M[i + 32] = R1[i];
            }
        }
    }
    private void GetEncryptResultOfByteArray(int[] data, byte[] value) {
        int i;
        int j;
        // 将存储64位二进制数据的数组中的数据转换为八个整数（byte）
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                value[i] += (data[(i << 3) + j] << (7 - j));
            }
        }
        for (i = 0; i < 8; i++) {
            value[i] %= 256;
            if (value[i] > 128) {
                value[i] -= 255;
            }
        }
    }

    private byte[] ByteDataFormat(byte[] data) {
        int len = data.length;
        int padlen = 8 - (len % 8);
        int newlen = len + padlen;
        byte[] newdata = new byte[newlen];
        System.arraycopy(data, 0, newdata, 0, len);
        for (int i = len; i < newlen; i++)
            newdata[i] = (byte) padlen;
        return newdata;
    }

    public byte[] DesEncrypt(byte[] des_key, byte[] des_data, int flag) {
        byte[] format_key = ByteDataFormat(des_key);
        byte[] format_data = ByteDataFormat(des_data);
        int datalen = format_data.length;
        int unitcount = datalen / 8;
        byte[] result_data = new byte[datalen];
        for (int i = 0; i < unitcount; i++) {
            byte[] tmpkey = new byte[8];
            byte[] tmpdata = new byte[8];
            System.arraycopy(format_key, 0, tmpkey, 0, 8);
            System.arraycopy(format_data, i * 8, tmpdata, 0, 8);
            byte[] tmpresult = UnitDes(tmpkey, tmpdata, flag);
            System.arraycopy(tmpresult, 0, result_data, i * 8, 8);
        }
        return result_data;
    }
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
    	int iLen = arrB.length;
    	// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
    	StringBuffer sb = new StringBuffer(iLen * 2);
    	for (int i = 0; i < iLen; i++) {
    		int intTmp = arrB[i];
    		// 把负数转换为正数
    		while (intTmp < 0) {
    			intTmp = intTmp + 256;
    		}
    		// 小于0F的数需要在前面补0
    		if (intTmp < 16) {
    			sb.append("0");
    		}
    		sb.append(Integer.toString(intTmp, 16));
    	}
    	return sb.toString();
    }
	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing </a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
    public String des_enc_8(String key, String data) throws Exception 
	{
    	if (data.length() > 8)
		{
			throw new Exception("长度不合法，超过8位长度");
		}
    	int len = data.length();
    	if (len < 8)
    	{
    		for(int i=0; i<8-len; i++)
    		{
    			data = data + " ";
    		}
    	}
        //int bytelen = data.getBytes().length;
        //byte[] result = new byte[(bytelen + 8) - (bytelen % 8)];

        byte[] result = DesEncrypt(key.getBytes(), data.getBytes(), 1);
        String str = byteArr2HexStr(result).substring(0, 16);
        return str;
    }
	public static String des_encrypt(String key_code, String txt)
	{
		txt = Native2AsciiUtil.native2Ascii(txt);
	  try 
	  {
	  	String result = "";
	  	
	  	String split_resut;
	  	String split_txt; 
	  	int len, pos;

        DES des = new DES();
	  	
	  	pos = 0;
	    while(true)
	    {
	    	len = txt.substring(pos).length();
	    	if(len == 0)
	    	{
	    		break;
	    	}
	    	else if(len < 8)
	    	{
	    		split_txt = txt.substring(pos);
	    		split_resut = des.des_enc_8(key_code, split_txt);
	    		result = result + split_resut;
	    		break;
	    	}
	    	else
	    	{
	    		split_txt = txt.substring(pos, pos+8);
	    		split_resut = des.des_enc_8(key_code, split_txt);
	    		result = result + split_resut;
	    	}
	    	
	    	pos = pos + 8;
	    }
	    return result;
	   
	  } catch (Exception e) 
	  {
		  {   
		   e.printStackTrace();   
		  }   
	  }   
	  return "";
		
	}

	public String des_dec_8(String key, String data) throws Exception 
	{
		if (data.length() != 16)
		{
			throw new Exception("长度不合法，密文应该为16位长度");
		}
		
        byte[] result = DesEncrypt(key.getBytes(), hexStr2ByteArr(data), 0);
        
        byte[] result1 = new byte[8];
        System.arraycopy(result, 0, result1, 0, 8);
         return new String(result1);
    }
    
	
	public static String des_descrypt(String key_code, String txt)
	{
		try
		{
			if(txt.length()%16 != 0)
			{
				throw new Exception("密文长度应该为16的整数倍");
			}
			
			DES des = new DES();
			
			String result="";
			String split_result="";
			
			int pos=0;
			String split_txt;
			
			while(pos < txt.length())
			{
				split_txt = txt.substring(pos, pos+16);
				if(split_txt.equals(""))
				{
					break;
				}
				split_result = des.des_dec_8(key_code, split_txt);
				result = result + split_result;
				
				pos = pos + 16;
			}
			return Native2AsciiUtil.ascii2Native(result).trim();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        return "";
	}
	public static void main(String[] args) throws Exception {
        String key = "CPIC0001";
        String data = "王涿是帅哥57190540091051不是61.01";
		
        String str1 = des_encrypt(key, data);
        //System.out.println("加密后：" + str1);
        String str2 = des_descrypt(key, str1);
        //System.out.println("解密后：" + str2);
        //System.out.println("加密前："+data);
        //System.out.println("是否相等："+str2.equals(data));
	}
	
    public static void main2(String[] args) throws Exception {
        String key = "abcdefgh";
        String data = "123";
        
        byte[] result; 
        byte[] bytekey = key.getBytes();
        byte[] bytedata = data.getBytes();
        
        //System.out.println("加密前:"+data);
        
        DES des = new DES();
        result = des.DesEncrypt(bytekey, bytedata, 1);
        String str = byteArr2HexStr(result);
        //System.out.println("加密后:"+str);
        
        String substr = str.substring(0, 16);
        //System.out.println("substr:"+substr);
        result = des.DesEncrypt(bytekey, hexStr2ByteArr(substr), 0);
        
        byte[] result1 = new byte[8];
        System.arraycopy(result, 0, result1, 0, 8);
        //System.out.println("解密后:"+(new String(result1)));
    }
}

/**功能：在外围解决中文问题，做本地字符到ascii码的转换
 * @author peter_wang
 *
 */
class Native2AsciiUtil {

	/**描述：将输入的字符串转换为Ascii值字符串，功能参照jdk的native2ascii.exe工具。
	 * @param nativeStr
	 * @return
	 */
	public static String native2Ascii(String nativeStr) {
		String asciiStr = "";
		//处理：把输入的中文转换为它的unicode值的字符串（这时内容中就不存在中文了），非中文不做处理
		for (int i = 0; i < nativeStr.length(); i++) {
			char c = (char) nativeStr.charAt(i);
			if(c < 128){
				asciiStr += c;
			}
			else{
				asciiStr += "\\u" + Integer.toString(c, 16);
			}
		}
		return asciiStr;
	}

	/**描述：将将输入的Ascii值字符串转换为本地字符串
	 * @param asciiStr
	 * @return
	 */
	public static String ascii2Native(String asciiStr) {
		String nativeStr = "";
		char[] charAry = new char[asciiStr.length()];
		
		//写到数组中，方便遍历
		for (int i = 0; i < charAry.length; i++) {
			charAry[i] = (char) asciiStr.charAt(i);
		}
		//处理：计算出中文的unicode值，写入到各个字符中，非中文不做处理
		for (int i = 0; i < charAry.length; i++) {
			char c = 0;
			if(charAry[i] == '\\'&&charAry[i+1] == 'u'){
				String code = asciiStr.substring(i+2, i+6);
				c = (char) Integer.parseInt(code,16); 
				i+=5;
			}	
			else{
				c = charAry[i];
			}
			nativeStr += c;
		}
		return nativeStr;
	}
}
