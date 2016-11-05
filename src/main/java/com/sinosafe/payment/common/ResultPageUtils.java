/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import java.util.Map;

/**
 * <doc>用于动态构建支付结果页面<doc>
 * @author 
 * @version 1.0 2013-9-8 上午08:57:37
 * @since 1.0
 * 
 */

public class ResultPageUtils {
	
	/**
	 * 支付成功页面信息构建
	 * @param parmMap
	 * @return
	 */
     public static String createSuccessPage(Map<String,String> parmMap){
    	 StringBuffer messages=new StringBuffer();
    	 messages.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    	 messages.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    	 messages.append("<head>");
    	 messages.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
    	 messages.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=device-dpi\"/>");
    	 messages.append("<title>支付结果</title>");
    	 messages.append("</head>");
    	 messages.append("<body>");
    	 messages.append("<br />");
    	 messages.append("<div style=\"height: 380px; width: 600px; margin: 0 auto; background-image: url(images/payresult/showresult_bj.jpg); font-size: 12px;\">");
    	 messages.append("<h1  style=\"margin: 0; padding: 0; border: 0; padding-top: 40px; padding-left: 130px; font-size: 14px; color: #1f8208; font-weight: bold\">您的交易支付成功，请返回商家网站查询详情</h1>");
    	 messages.append("<table style=\"margin: 0; padding: 0; border: 0; margin-left: 130px; margin-top: 30px; width: 70%; color: #323232; line-height: 25px;\"	height=\"78\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
    	 messages.append("<tr>");
    	 messages.append("<td width=\"25%\" align=\"right\">到账通知时间：</td>");
    	 messages.append("<td width=\"75%\" align=\"left\">"+parmMap.get("dealTime")+"&nbsp;</td>");
    	 messages.append("</tr>");
    	 messages.append("<tr>");
    	 messages.append("<td align=\"right\">系统交易号：</td>");
    	 messages.append("<td align=\"left\">"+parmMap.get("orderId")+"</td>");
    	 messages.append("</tr>");
    	 messages.append("</table>");
    	 messages.append("<table style=\"background: url(images/payresult/showresulttz_bg.jpg) no-repeat; margin: 0; padding: 0; border: 0; margin-left: 130px; width: 70%; height: 40px\">");
    	 messages.append("<tr>");
    	 messages.append("<td>商品信息<span	style=\"font-size: 14px; color: #303030; font-weight: bold; margin-left: 5px;\">"+parmMap.get("orderdesc")+"&nbsp;</span></td>");
    	 messages.append("<td>&nbsp;</td>");
    	 messages.append("<td>支付金额<span	style=\"font-size: 16px; color: #c37510; font-weight: bolder; margin-left: 5px;\">"+parmMap.get("payAmount")+"</span>&nbsp;元</td>");
    	 messages.append("</tr>");
    	 messages.append("</table>");
    	 messages.append("<div style=\" margin-top:20px; height:35px; width:550px;\">");
    	 messages.append("<div style=\"margin-left:1px;width:50px; float:right; margin-top:8px; color:#6d6d6d\"> </div>");
    	 messages.append("<div style=\" width:120px;float:right;\">");
    	 messages.append(" <a href=\"javascript:window.close()\"><img src=\"images/payresult/close_button.jpg\"  border=\"0\"/></a></div>");
    	 messages.append("</div>");
    	 messages.append("<div style=\"margin:0 auto; float:right; margin-top:50px;height:auto;width:540px;color:#6d6d6d\"><div>");
    	 messages.append("<div id=\"footer\">Copyright &copy; 2011-2014 FNDSOFT Corporation. All rights reserved. 菲耐得版权所有<br />使用过程如有任何问题，请联系菲耐得客服邮箱：<a href=\"mailto:help@fndsoft.cn\" target=\"_blank\">help@fndsoft.cn</a></div>");
    	 messages.append("</div></div></div>");
    	 messages.append("</body>");
    	 messages.append("</html>");
    	 return messages.toString();
     }
     
     /**
      * 支付失败页面信息构建
      * @param parmMap
      * @return
      */
     public static String createErrorPage(String message){
    	 StringBuffer messages=new StringBuffer();
    	 messages.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    	 messages.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    	 messages.append("<head>");
    	 messages.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
    	 messages.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=device-dpi\"/>");
    	 messages.append("<title>支付结果</title>");
    	 messages.append("</head>");
    	 messages.append("<body style=\"width:100%;height:100%;\">");
    	 messages.append("<div style=\"font-size:15px;float:right;width:100%;font-weight:normal;\">");
    	 messages.append("<a href=\"http://www.fndsoft.com/\">帮助中心</a>");
    	 messages.append("</div>");
    	 messages.append("<div style=\"border-top: 0px solid #D9DBDA;font-weight: bold; font-size:19px;text-align: center;color:#FF0000;\">");
    	 messages.append("<div style=\" padding-top: 50px;padding-left:5%;padding-right:5%;\">"+message+"</div>");
    	 messages.append("</div>");
    	 messages.append("<div style=\"text-align:center; padding-top: 2%;\">");
    	 messages.append("<input type=\"button\" value=\"关闭窗口\" onclick=\"javascript:window.close()\"/>");
    	 messages.append("</div>");
    	 messages.append("<div style=\"border-top: 0px solid #D9DBDA; clear: both; display: block; font-size:12px; margin-left:auto; margin-right: auto; padding-left:5%;padding-right:5%;padding-top:2%; text-align: center;\">");
    	 messages.append("<div id=\"footer\">Copyright &copy; 2011-2014 FNDSOFT Corporation. All rights reserved. 菲耐得版权所有。使用过程如有任何问题，请联系菲耐得客服邮箱：<a href=\"mailto:help@fndsoft.cn\" target=\"_blank\">help@fndsoft.cn</a></div>");
    	 messages.append("</div>");
    	 messages.append("</body></html>");
    	 return messages.toString();
     }
     
     
     public static String createBankLinkPage(String message) {
		StringBuffer messages = new StringBuffer();
		messages.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		messages.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		messages.append("<head>");
		messages.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\" />");
		messages.append("<title>正在跳转,请稍后...</title>");
		messages.append("</head>");
		messages.append("<body>");
		messages.append("<div style=\"height: 100px; width: 150px; margin-top:250px; margin-left:600px; background-image: url(api/loading.gif); font-size: 12px;\"></div>");
		messages.append(message);
		messages.append("</body></html>");
		return messages.toString();
	}
}
