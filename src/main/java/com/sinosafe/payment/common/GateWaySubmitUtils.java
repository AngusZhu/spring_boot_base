/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sinosafe.payment.excepiton.SDKRuntimeException;
import com.sinosafe.payment.service.pay.alipaywap.util.AlipaySubmit;
import com.sinosafe.payment.service.pay.wx.CommonUtil;
import com.sinosafe.payment.service.pay.wx.WxPayHelper;
import com.sinosafe.payment.vo.BankGateWayRequest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

/**
 * <doc>银行支付网关工具类用于产生请求FORM<doc>
 *
 * @author chensf
 * @version 1.0 2013-8-30 上午12:44:24
 * @since 1.0
 */

public class GateWaySubmitUtils {
    private static final Logger logger = Logger.getLogger(GateWayCore.class);

    public static String createSubmibFormFor_ALIPAY(BankGateWayRequest bankRequest) {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("payment_type", "1");// 支付类型,固定值1
        sParaTemp.put("out_trade_no", bankRequest.getOrderId());// 商户支付申请号
        sParaTemp.put("subject", bankRequest.getOrderName());// 订单名称
        sParaTemp.put("total_fee", bankRequest.getOrderAmount());// 订单金额
        sParaTemp.put("body", "");// 订单描述
        sParaTemp.put("show_url", "");// bankRequest.getGateWay().getShow_url());// 商品展示地址
        sParaTemp.put("exter_invoke_ip", "");
        sParaTemp.put("paymethod", (StringUtils.hasText(bankRequest.getBankCode()) == true ? "bankPay" : "directPay"));
        if (!"ALIPAY".equals(bankRequest.getBankCode()) && (!StringUtils.hasText(bankRequest.getGateWay().getOrderBankcode()))) {// 设置默认支付银行，如果配置的非ALIPAY则为使用指定的银行如CMB，否则如果配置的有值的话则只能使用此银行
            sParaTemp.put("defaultbank", bankRequest.getBankCode());
        }
        sParaTemp.put("partner", bankRequest.getGateWay().getRequestId());// 请求用户ID类似用户名
        sParaTemp.put("return_url", "");// 前台通知页面地址
        sParaTemp.put("notify_url", bankRequest.getGateWay().getNotifyUrl());// 后台通知接口地址
        sParaTemp.put("seller_email", bankRequest.getGateWay().getAccountNo());// 卖家帐号也支付宝账号
        sParaTemp.put("_input_charset", bankRequest.getGateWay().getCharset());// 字符编码格式GBK或UTF-8
        String strButtonName = "确认";
        return buildFormFor_ALIPAY(sParaTemp, strButtonName, bankRequest);
    }

    /**
     * 构建提交支付宝页面的form表单
     */
    private static String buildFormFor_ALIPAY(Map<String, String> sParaTemp,
                                              String strButtonName, BankGateWayRequest bankRequest) {
        Map<String, String> sPara = buildRequestParaFor_ALIPAY(sParaTemp,
                bankRequest);
        List<String> keys = new ArrayList<String>(sPara.keySet());
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\""
                + bankRequest.getGateWay().getPcRequestUrl()
                + "_input_charset="
                + bankRequest.getGateWay().getCharset()
                + "\" method=\""
                + "post" + "\">");
        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name
                    + "\" value=\"" + value + "\"/>");
        }

        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName
                + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
        return sbHtml.toString();
    }

    /**
     * 将原Map参数后面增加sign参数，签名方法的字符串为原参数构建的字符串加上提供给客户 的KEY
     *
     * @param sParaTemp
     * @return
     */
    private static Map<String, String> buildRequestParaFor_ALIPAY(
            Map<String, String> sParaTemp, BankGateWayRequest bankRequest) {
        Map<String, String> sPara = paraFilterFor_ALIPAY(sParaTemp);// 过虑参数空值
        String mysign = buildMysignFor_ALIPAY(sPara, bankRequest);// 对非空值进行MD5签名
        sPara.put("sign", mysign);
        sPara.put("sign_type", bankRequest.getGateWay().getSignType());
        return sPara;
    }

    /**
     * 过虑参数空值,另外后面校验时也不应该把sign和sign_type放置在校验的参数里
     *
     * @param sArray
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, String> paraFilterFor_ALIPAY(
            Map<String, String> sArray) {
        Map result = new HashMap();
        if ((sArray == null) || (sArray.size() <= 0)) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = (String) sArray.get(key);
            if ((value == null) || ("".equals(value))
                    || (key.equalsIgnoreCase("sign"))
                    || (key.equalsIgnoreCase("sign_type"))) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 对输入的参数进行签名
     *
     * @param sArray
     * @return
     */
    private static String buildMysignFor_ALIPAY(Map<String, String> sArray,
                                                BankGateWayRequest bankRequest) {
        String prestr = createLinkString(sArray);
        logger.debug("用户请求参数值(已排序及过虑空值)" + prestr);
        prestr = prestr + bankRequest.getGateWay().getSignKey();
        String mysign = MD5.MD5Encode(prestr, bankRequest.getGateWay()
                .getCharset());
        return mysign;
    }

    /**
     * 将Map参数构建成=?&连接的字符串
     *
     * @param params
     * @return
     */
    private static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String createSubmibFormFor_ALIPAYWAP(BankGateWayRequest bankRequest) {
        String sHtmlText = "";
        String req_dataToken = "";
        String call_back_url = "";
        String merchant_url = "";
        StringBuffer req_datam = new StringBuffer();

        try {//业务数据

            String req_id = "";//请求号ISO-8859-1
            req_datam.append("<direct_trade_create_req>");
            req_datam.append("<notify_url>").append(bankRequest.getGateWay().getNotifyUrl()).append("</notify_url>");////服务器异步通知页面路径
            req_datam.append("<call_back_url>").append("").append("</call_back_url>");////页面跳转同步通知页面路径
            req_datam.append("<seller_account_name>").append(bankRequest.getGateWay().getAccountNo()).append("</seller_account_name>");////卖家支付宝帐户
            req_datam.append("<out_trade_no>").append(bankRequest.getOrderId()).append("</out_trade_no>");//商户订单号
            req_datam.append("<subject>").append("保险产品（支付申请号：" + bankRequest.getOrderId() + "）").append("</subject>");//订单名称
            //req_datam.append("<total_fee>").append(new BigDecimal(bankRequest.getOrderAmount()).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).toString()).append("</total_fee>");// 支付金额，以分为单位
            req_datam.append("<total_fee>").append(new BigDecimal(bankRequest.getOrderAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("</total_fee>");// 支付金额，以分为单位

            req_datam.append("<merchant_url>").append(merchant_url).append("</merchant_url>");////操作中断返回地址
            req_datam.append("</direct_trade_create_req>");
            req_dataToken = req_datam.toString();

            Map<String, String> sParaTempToken = new HashMap<String, String>();
            sParaTempToken.put("service", "alipay.wap.trade.create.direct");
            sParaTempToken.put("partner", bankRequest.getGateWay().getRequestId());
            sParaTempToken.put("_input_charset", bankRequest.getGateWay().getCharset());
            sParaTempToken.put("sec_id", bankRequest.getGateWay().getSignType());
            sParaTempToken.put("format", "xml");
            sParaTempToken.put("v", "2.0");
            sParaTempToken.put("req_id", req_id);
            sParaTempToken.put("req_data", req_dataToken);
            //System.out.println(sParaTempToken.toString());
            //把请求参数打包成数组
            Map<String, String> sParaTemp;

            //建立请求 http://wappaygw.alipay.com/service/rest.htm?
            String sHtmlTextToken = AlipaySubmit.buildRequest(bankRequest.getGateWay(), sParaTempToken);
            //URLDECODE返回的信息
            sHtmlTextToken = URLDecoder.decode(sHtmlTextToken, bankRequest.getGateWay().getCharset());
            //获取token
            String request_token = AlipaySubmit.getRequestToken(bankRequest.getGateWay(), sHtmlTextToken);
            //业务详细
            String req_data = "<auth_and_execute_req><request_token>" + request_token + "</request_token></auth_and_execute_req>";
            sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
            sParaTemp.put("partner", bankRequest.getGateWay().getRequestId());
            sParaTemp.put("_input_charset", bankRequest.getGateWay().getCharset());
            sParaTemp.put("sec_id", bankRequest.getGateWay().getSignType());
            sParaTemp.put("format", "xml");
            sParaTemp.put("v", "2.0");
            sParaTemp.put("req_data", req_data);
            sHtmlText = AlipaySubmit.buildRequest(bankRequest.getGateWay(), sParaTemp, "get", "确认");


        } catch (UnsupportedEncodingException e) {
            logger.info("" + e.getMessage());
        } catch (Exception e) {
            logger.info("" + e.getMessage());
        }
        return sHtmlText;
    }

    /**
     * 微信jsapi支付请求网关
     *
     * @param bankRequest
     * @return
     * @throws SDKRuntimeException
     * @throws UnsupportedEncodingException
     */
    public static String createSubmibFormFor_WXJSAPI(BankGateWayRequest bankRequest) throws SDKRuntimeException, UnsupportedEncodingException {
        Map<String, String> sParaTemp = new HashMap<String, String>();

        String prepay_id = "";
        String trade_type = "JSAPI";
        //调用统一下单api，获取prepay_id
        prepay_id = WxPayHelper.WXUnifiedOrderApi(trade_type, bankRequest);

        String packageValue = "prepay_id=" + prepay_id;    //统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
        String timeStamp = Long.toString(new Date().getTime() / 1000); //时间戳
        String noncestr = UUID.randomUUID().toString().replace("-","");//生成随机字符串

        HashMap<String, String> nativeObj = new HashMap<String, String>();//构建签名 paySign的所需字段,除了paySign字段
        nativeObj.put("appId", bankRequest.getGateWay().getRequestId());
        nativeObj.put("timeStamp", timeStamp);
        nativeObj.put("package", packageValue);
        nativeObj.put("signType", bankRequest.getGateWay().getSignType());//签名方式
        nativeObj.put("nonceStr", noncestr);

        sParaTemp.put("appId", bankRequest.getGateWay().getRequestId());//公众号id
        sParaTemp.put("timeStamp", timeStamp);
        sParaTemp.put("nonceStr", noncestr);  //随机字符串
        sParaTemp.put("package", packageValue); //订单详情扩展字符串
        sParaTemp.put("signType", bankRequest.getGateWay().getSignType());    //		签名方式
        sParaTemp.put("paySign", WxPayHelper.GetBizSign(nativeObj, bankRequest.getGateWay().getSignKey()));
        return buildFormFor_WXJSAPI(sParaTemp, "微信支付", bankRequest);
    }

    private static String buildFormFor_WXJSAPI(Map<String, String> sParaTemp,
                                               String strButtonName, BankGateWayRequest bankRequest) {
        StringBuffer sbHtml = new StringBuffer();
        //根据官方拼接js调用微信jsapi
        sbHtml.append("<script type=\"text/javascript\">function onBridgeReady() {WeixinJSBridge.invoke(\'getBrandWCPayRequest\',{\"appId\"" + ":\"" + sParaTemp.get("appId") + "\"," + "\"timeStamp\"" + ":\"" + sParaTemp.get("timeStamp") + "\","
                + "\"nonceStr\"" + ":\"" + sParaTemp.get("nonceStr") + "\"," + "\"package\"" + ":\"" + sParaTemp.get("package") + "\","
                + "\"signType\"" + ":\"" + sParaTemp.get("signType") + "\"," + "\"paySign\"" + ":\"" + sParaTemp.get("paySign") + "\"},function(res) {" +
                "WeixinJSBridge.log(res.err_msg);alert(res.err_code+res.err_desc);});}");
        sbHtml.append("if (typeof WeixinJSBridge == \"undefined\"){");
        sbHtml.append("if( document.addEventListener ){");
        sbHtml.append("document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);");
        sbHtml.append("}else if (document.attachEvent){");
        sbHtml.append("document.attachEvent('WeixinJSBridgeReady', onBridgeReady);");
        sbHtml.append("document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);");
        sbHtml.append("}");
        sbHtml.append("}else{");
        sbHtml.append("onBridgeReady();");
        sbHtml.append("}");
        sbHtml.append("</script>JSAPI");
        logger.debug("构建微信JSAIP支付表单:" + sbHtml.toString());
        return sbHtml.toString();
    }

    /**
     * 微信native扫码支付请求同意下单api，返回code_url,加密返回
     *
     * @param bankRequest
     * @return
     * @throws SDKRuntimeException
     * @throws UnsupportedEncodingException
     */
    public static String createQcodePageFor_WXNATIVE(
            BankGateWayRequest bankRequest) throws SDKRuntimeException, UnsupportedEncodingException {
        //调用JSAPI支付接口，所需字段。从调用统一下单接口返回结果获取
        String code_url = "";
        String trade_type = "NATIVE";
        //返回支付链接
        code_url = WxPayHelper.WXUnifiedOrderApi(trade_type, bankRequest);
        String returnMessage = code_url + "WEIXIN";
        return returnMessage;// 构建二维码页面
    }

    /**
     * 微信APP支付请求网关
     *
     * @param bankRequest
     * @return
     * @throws SDKRuntimeException
     * @throws UnsupportedEncodingException
     */
    public static String createSubmibFormFor_WXNATIVEAPP(BankGateWayRequest bankRequest) throws SDKRuntimeException, UnsupportedEncodingException {
        Map<String, String> sParaTemp = new HashMap<String, String>();

        String trade_type = "APP";
        //调用统一下单api，获取prepay_id
        String prepay_id = WxPayHelper.WXUnifiedOrderApi(trade_type, bankRequest); //调用微信统一下单Api
        String packageValue = "Sign=WXPay";    //微信app支付package
        String timeStamp = Long.toString(new Date().getTime() / 1000); //时间戳
        String noncestr = CommonUtil.CreateNoncestr();//生成随机字符串

        HashMap<String, String> nativeObj = new HashMap<String, String>();//构建签名 paySign的所需字段,除了paySign字段
        nativeObj.put("appid", bankRequest.getGateWay().getRequestId());
        nativeObj.put("partnerid", bankRequest.getGateWay().getInsuMidno());
        nativeObj.put("prepayid", prepay_id);//调用微信统一下单api，返回字段prepay_id
        nativeObj.put("package", packageValue); //固定值  Sign=WXPay
        nativeObj.put("noncestr", noncestr);    //随机字符串
        nativeObj.put("timestamp", timeStamp);    //时间戳
        nativeObj.put("sign", WxPayHelper.GetBizSign(nativeObj, bankRequest.getGateWay().getSignKey()));  //签名
        nativeObj.put("WEIXINAPP", "WEIXINAPP");
        nativeObj.put("orderId", bankRequest.getOrderId());
        return buildFormFor_WXAPIAPP(nativeObj, "微信支付", bankRequest);
    }

    private static String buildFormFor_WXAPIAPP(Map<String, String> sParaTemp,
                                                String strButtonName, BankGateWayRequest bankRequest) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : sParaTemp.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String newStr = sb.toString();
        if (newStr.contains("&")) {
            newStr = newStr.substring(0, newStr.length() - 1);
        }
        String url = newStr;
        return url.toString();
    }
}
