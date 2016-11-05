package com.sinosafe.payment.common;

public class SecurityUtil {


    /**
	 * 处理特殊字符(Cross-site scripting (XSS))
	 *  // Filter the HTTP response using SecurityUtil.outputfilter 
	 *  PrintWriter out = response.getWriter(); 
	 *  // set output response
	 * 	out.write(SecurityUtil.outputfilter(response)); 
	 * 	out.close();
	 */
	public static String outputfilter(String value) {
		if (value == null || value.trim().equals("")) {
			return null;
		}
		StringBuffer result = new StringBuffer(value.length());
		for (int i=0; i<value.length(); ++i) {
			switch (value.charAt(i)) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '"':
				result.append("&quot;");
				break;
			case '\'':
				result.append("&#39;");
				break;
			case '%':
				result.append("&#37;");
				break;
			case ';':
				result.append("&#59;");
				break;
			case '(':
				result.append("&#40;");
				break;
			case ')':
				result.append("&#41;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '+':
				result.append("&#43;");
				break;
			default:
				result.append(value.charAt(i));
			break;
			}
		}
			return result.toString();
	}


	
   /**
	 * 处理特殊字符(HTTP response splitting)
	 * 在HTTP响应头文件中包含未经过校验的数据会导致cache-poisoning，cross-site scripting，cross-user
	 * defacement或者page hijacking攻击。
	 * 应用程序应该屏蔽任何肯定要出现在HTTP响应头中、含有特殊字符的输入，特别是CR（回车符，也可由%0d或\r提供）和LF（换行符，也可由%0a或\n提供）字符，将它们当作非法字符。
	 * 
	 * @param str
	 * @return
	 */
 public static String fixHttpRS(String str){
	 if (str==null){
	     return null;
	   }
	   String str_temp = str;
	   
	   while(true){
		   if ((str_temp.indexOf("CR")==-1)&&(str_temp.indexOf("%0d")==-1)&&(str_temp.indexOf("\r")==-1)
				   &&(str_temp.indexOf("LF")==-1)&&(str_temp.indexOf("%0a")==-1)&&(str_temp.indexOf("\n")==-1)){
			   break;
		   }
		   if (str_temp.indexOf("CR")!=-1){
			   str_temp = str_temp.replaceAll("CR", "");
	   		}
 			if (str_temp.indexOf("%0d")!=-1){
 				str_temp = str_temp.replaceAll("%0d", "");
			}
 			if (str_temp.indexOf("\r")!=-1){
 				str_temp = str_temp.replaceAll("\r", "");
			}
			if (str_temp.indexOf("LF")!=-1){
				str_temp = str_temp.replaceAll("LF", "");
			}
			if (str_temp.indexOf("%0a")!=-1){
				str_temp = str_temp.replaceAll("%0a", "");
			}
			if (str_temp.indexOf("\n")!=-1){
				str_temp = str_temp.replaceAll("\n", "");
			}
	   }

	    return str_temp.toString();
 }




 
}
