package com.sinosafe.payment.common;


public class Basic2Config

{
	
	  ///////////////////////////////////////错误代码定义
	  public static String INFOFORMAT="X-X-X-X"; 
	  public static String E01001="请求参数异常";            
	  public static String E01002="校验码无效";              
	  public static String E02001="用户无效";                
	  public static String E02002="用户无权限";              
	  public static String E03001="签名编码错误";            
	  public static String E03002="用户签名校验失败";        
	  public static String E03003="交易签名校验失败";        
	  public static String E04001="终端无权限";              
	  public static String E05001="支付渠道配置错误";        
      public static String F01001="系统异常";                
	  public static String W01001="支付申请不存在";                      
	  public static String W01002="支付申请不存在";              
	  public static String W01003="支付申请已过期";          
	  public static String W01004="支付申请已撤销";                   
	  public static String W01005="支付申请已完成";                
	  public static String W01006="支付申请金额错误";        
	  public static String W02001="不支持此银行";                   
	  public static String W02002="当前支付渠道不支持此银行";
	  public static String W02003="不支持手机支付";               
	  public static String W02004="当前渠道不支持手机支付";  
	  public static String W03001="支付方式不正确";
	  public static String W04001="金额异常";
	  public static String W04002="暂未收到银行的到账通知";
	  public static String W01007="业务系统业务校验失败";
	  public static String W05002="缴费前需完成电话确认";
	  public static String W05001="投保单缴费状态锁定";
	  public static String W03002="核心不支持非车险投保单刷卡缴费";
	  

  public static String errorInfo(String errr,String errrinfo,String orderid,String no){

	  String format = Basic2Config.INFOFORMAT;
	  String[] predx = format.split("X");
	  String message = "";

	  int i = 1;
	  message = errr+predx[i++]+errrinfo+predx[i++]+orderid+predx[i++]+no;
	
	 return message;

  }
}