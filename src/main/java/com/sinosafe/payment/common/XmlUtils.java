/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import com.sinosafe.payment.service.bank.message.BankConsumeResponse;
import com.sinosafe.payment.vo.BankQueryRequest;
import com.sinosafe.payment.vo.BankQueryResponse;
import com.sinosafe.payment.vo.RecvMerchantInfoResponse;
import com.sinosafe.payment.vo.WebParam;
import org.apache.log4j.Logger;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * XML报文构建工具类
 *
 * @author chensf
 * @version 1.0 2013年8月21日21:25:34
 * @since 1.0
 */

public class XmlUtils {
    private static final Logger logger = Logger.getLogger(XmlUtils.class);

    /**
     * 调用收款请求时根据HttpServletRequest的请求参数构建用户请求报文
     *
     * @param parmMap
     * @return
     */
    public String build_RecvMercharInfoRequest(Map<String, String> parmMap) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element title = document.addElement("PACKET").addAttribute("TYPE",
                "REQUEST");
        Element head = title.addElement("HEAD");
        head.addElement("REQUEST_TYPE")
                .addText(
                        StringUtils.hasText(parmMap.get("trade_code")) == true ? parmMap
                                .get("trade_code") : "");
        head.addElement("USER").addText(
                StringUtils.hasText(parmMap.get("partnerID")) == true ? parmMap
                        .get("partnerID") : "");
        head.addElement("PASSWORD").addText(
                StringUtils.hasText(parmMap.get("password")) == true ? parmMap
                        .get("password") : "");
        Element body = title.addElement("REQUEST_INFO");
        List<String> keys = new ArrayList<String>(parmMap.keySet());
        String[] charset = {"trade_code", "partnerID", "password"};
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if (Arrays.asList(charset).contains(key)) {
                continue;
            }
            String value = (String) parmMap.get(key);
            body.addElement(key.toUpperCase()).addText(
                    StringUtils.hasText(value) == true ? value : "");
        }
        return document.asXML();
    }

    /**
     * 构建调用支付平台银行查询请求报文
     *
     * @param request
     * @return
     */
    public String build_BankQueryRequest(BankQueryRequest request) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element title = document.addElement("PACKET").addAttribute("TYPE",
                "REQUEST");
        Element head = title.addElement("HEAD");
        head.addElement("TRAN_CODE").addText("10");
        head.addElement("USER").addText(request.getUser());
        head.addElement("PASSWORD").addText(request.getPassword());
        Element body = title.addElement("BODY");
        Element base = body.addElement("BASE");
        base.addElement("BANK_CODE").addText(
                StringUtils.hasText(request.getBank_code()) == true ? request
                        .getBank_code() : "");
        base.addElement("INSURE_ID").addText(
                StringUtils.hasText(request.getInsure_id()) == true ? request
                        .getInsure_id() : "");
        base.addElement("MIDNO").addText(
                StringUtils.hasText(request.getMidno()) == true ? request
                        .getMidno() : "");
        base.addElement("TIDNO").addText(
                StringUtils.hasText(request.getTidno()) == true ? request
                        .getTidno() : "");
        base.addElement("BK_ACCT_DATE").addText(request.getBk_acct_date());
        base.addElement("BK_ACCT_TIME").addText(request.getBk_acct_time());
        base.addElement("BK_SERIAL").addText(request.getBk_serial());
        base.addElement("BK_TRAN_CHNL").addText(
                StringUtils.hasText(request.getBk_tran_chnl()) == true ? request
                        .getBk_tran_chnl() : "WEB");
        base.addElement("PAY_APP_NO").addText(request.getPay_app_no());
        base.addElement("CHECKCODE").addText(request.getCheckcode()==null?"":request.getCheckcode());
        base.addElement("TA_NO").addText(request.getTaNo());
        return document.asXML();
    }

    /**
     * 解析银行返回报文
     *
     * @param responseXML
     * @return
     * @throws UnsupportedEncodingException
     * @throws DocumentException
     */
    public BankQueryResponse parse_BankQueryResponse(String responseXML)
            throws UnsupportedEncodingException, DocumentException {
        SAXReader reader = new SAXReader(false);// 初始化新的SAXReader
        BankQueryResponse response = new BankQueryResponse();
        InputStream in = new ByteArrayInputStream(responseXML.getBytes("UTF-8"));
        Document document = reader.read(in);
        Element root = document.getRootElement();
        Element head = root.element("HEAD");
        response.setTradeType(head.elementTextTrim("TRAN_CODE"));
        response.setRcpt_no(head.elementTextTrim("RCPT_NO"));
        response.setRslt_code(head.elementTextTrim("RSLT_CODE"));
        response.setRslt_msg(head.elementTextTrim("RSLT_MSG"));
        Element body = root.element("BODY");
        Element base = body.element("BASE");
        response.setPay_apply_no(base.elementTextTrim("PAY_APP_NO"));
        response.setCheck_code(base.elementTextTrim("CHECK_CODE"));
        response.setAmount(base.elementTextTrim("AMOUNT"));
        response.setRemark(base.elementTextTrim("REMARK"));
        response.setTaNo(base.elementTextTrim("TA_NO"));
        return response;
    }

    // 设置节点的属性
    public static void setAttr(Element el, String name, String value)
            throws Exception {
        el.addAttribute(name, value);
    }

    public static Document getDocument(Reader reader) {
        try {
            return sr.read(reader);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Document getDocument(InputStream reader) throws Exception {
        try {
            return sr.read(reader);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Document getDocument(String msg) throws Exception {
        StringReader str = new StringReader(msg);
        return getDocument(str);
    }

    public static Element createElement(String name) throws Exception {
        return factory.createElement(name);
    }

    // 设置节点的属性
    public static void setElement(Element el, String name, String value)
            throws Exception {
        el.addElement(name).setText(value);
    }

    // 获取节点属性
    public static String getAttr(Element el, String attr) throws Exception {
        String attrvalue = "";
        try {
            attrvalue = el.attributeValue(attr);
        } catch (Exception e) {
            System.out.println("======获取节点属性获取节点属性获取节点属性========");
            throw e;
        }
        return attrvalue;
    }

    private static DocumentFactory factory = new DocumentFactory();
    private static SAXReader sr = new SAXReader();

    public static String getElement(Element el, String attr) throws Exception {
        String value = "";
        try {
            value = el.elementText(attr);
        } catch (Exception e) {
            System.out.println("======获取节点属性获取节点属性获取节点属性========");
            throw e;
        }
        return value;
    }

    // 获取子元素的值
    public static String getText(Element el, String child) throws Exception {
        Element temp = el.element(child);
        if (temp == null)
            return null;
        else
            return temp.getText().trim();
    }

    public static Document createDocument() throws Exception {
        return factory.createDocument();
    }

    /**
     * 将用户请求返回参数拼装成报文保单到数据库
     *
     * @param response
     * @return
     */
    public String build_RecvMercharInfoResponse(
            RecvMerchantInfoResponse response) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element title = document.addElement("PACKET").addAttribute("TYPE",
                "RESPONSE");
        Element head = title.addElement("HEAD");
        head.addElement("RESPONSE_TYPE").addText("E1");
        head.addElement("ORDER_ID").addText(
                StringUtils.hasText(response.getOrder_id()) == true ? response
                        .getOrder_id() : "");
        Element body = title.addElement("RESPONSE_INFO");
        body.addElement("RCPT_NO").addText(response.getRcpt_no());
        body.addElement("CHECK_CODE")
                .addText(
                        StringUtils.hasText(response.getCheck_code()) == true ? response
                                .getCheck_code() : "");
        body.addElement("ORDER_AMOUNT")
                .addText(
                        StringUtils.hasText(response.getOrder_amount()) == true ? response
                                .getOrder_amount() : "");
        body.addElement("REQUEST_SING")
                .addText(
                        StringUtils.hasText(response.getRequest_sing()) == true ? response
                                .getRequest_sing() : "");
        body.addElement("RESPONSE_SIGN")
                .addText(
                        StringUtils.hasText(response.getResponse_sign()) == true ? response
                                .getResponse_sign() : "");
        body.addElement("VERIFYRE_RESULE")
                .addText(
                        StringUtils.hasText(response.getVerify_resule()) == true ? response
                                .getVerify_resule() : "");
        body.addElement("RSLT_CODE").addText(
                StringUtils.hasText(response.getRslt_code()) == true ? response
                        .getRslt_code() : "");
        body.addElement("RSLT_MSG").addText(
                StringUtils.hasText(response.getRslt_msg()) == true ? response
                        .getRslt_msg() : "");
        body.addElement("ACTION_URL")
                .addText(
                        StringUtils.hasText(response.getAction_url()) == true ? response
                                .getAction_url() : "");
        body.addElement("ACTION_CODE")
                .addText(
                        StringUtils.hasText(response.getAction_code()) == true ? response
                                .getAction_code() : "");
        body.addElement("ACTION_MSG")
                .addText(
                        StringUtils.hasText(response.getAction_msg()) == true ? response
                                .getAction_msg() : "");
        return document.asXML();
    }

    /**
     * 将微信支付完成传过来的参数构建成调用支付平台的参数
     */
    public Map<String, String> buildConsumeMapFOR_WX(Map<String, String> parmMap) {
        Map<String, String> parmMapTemp = new HashMap<String, String>();
        parmMapTemp.put("MIDNO", parmMap.get("mch_id"));//商户号
        parmMapTemp.put("bk_acct_date", parmMap.get("time_end").substring(0, 8));//交易日期
        parmMapTemp.put("bk_acct_time", parmMap.get("time_end").substring(8, 14));//交易时间
        parmMapTemp.put("trade_no", parmMap.get("transaction_id"));//微信订单号
        parmMapTemp.put("pay_app_no", parmMap.get("out_trade_no"));//订单号
        parmMapTemp.put("amount", new BigDecimal(parmMap.get("total_fee")).toString());
        parmMapTemp.put("batch_no", "000");
        parmMapTemp.put("cash_no", "000");
        parmMapTemp.put("card_no", "000");//手机号
        parmMapTemp.put("sub_amount", new BigDecimal(parmMap.get("total_fee")).toString());
        return parmMapTemp;
    }

    /**
     * 构建调用支付平台支付确认报文
     *
     * @param parmMap
     * @param constants
     * @return
     * @webparam 判断各渠道来源
     */
    public String build_BankConsumeRequest(Map<String, String> parmMap,
                                           Constants constants, WebParam webParam) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element title = document.addElement("PACKET").addAttribute("TYPE",
                "REQUEST");
        Element head = title.addElement("HEAD");
        head.addElement("TRAN_CODE").addText("20");
        head.addElement("USER").addText(constants.getUserId());
        head.addElement("PASSWORD").addText(constants.getPassword());
        Element body = title.addElement("BODY");
        Element base = body.addElement("BASE");
        base.addElement("BANK_CODE").addText(constants.getBankCode());
        base.addElement("INSURE_ID").addText(constants.getInsureId());
        base.addElement("MIDNO").addText(parmMap.get("MIDNO"));
        base.addElement("TIDNO").addText(webParam.getTidNo());//不再使用统一的终端号，快钱、支付宝分开
        base.addElement("BK_ACCT_DATE").addText(parmMap.get("bk_acct_date"));
        base.addElement("BK_ACCT_TIME").addText(parmMap.get("bk_acct_time"));
        base.addElement("BK_SERIAL").addText(parmMap.get("trade_no"));//空值
        base.addElement("BK_TRAN_CHNL").addText("WEB");
        base.addElement("PAY_APP_NO").addText(parmMap.get("pay_app_no"));
        base.addElement("AMOUNT").addText(parmMap.get("amount"));
        base.addElement("PAYCOUNT").addText("1");
        base.addElement("CHECK_OPCODE");
        base.addElement("CHECK_OPNAME");
        Element detail = body.addElement("DETAILS");
        Element payinfo = detail.addElement("PAYINFO");
        payinfo.addElement("BATCH_NO").addText(parmMap.get("batch_no"));
        payinfo.addElement("CASH_NO").addText(parmMap.get("cash_no"));
        payinfo.addElement("CARD_NO").addText(parmMap.get("card_no"));
        payinfo.addElement("SUB_AMOUNT").addText(parmMap.get("sub_amount"));
        return document.asXML();
    }


    public String build_BankConsumeRequest(Map<String, String> parmMap) {
        Constants constants = (Constants) SpringContextUtil.getBean("constants");
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element title = document.addElement("PACKET").addAttribute("TYPE", "REQUEST");
        Element head = title.addElement("HEAD");
        head.addElement("TRAN_CODE").addText("20");
        head.addElement("USER").addText(constants.getUserId());
        head.addElement("PASSWORD").addText(constants.getPassword());
        Element body = title.addElement("BODY");
        Element base = body.addElement("BASE");
        base.addElement("BANK_CODE").addText(constants.getBankCode());
        base.addElement("INSURE_ID").addText(constants.getInsureId());
        base.addElement("MIDNO").addText(parmMap.get("midno"));
        Date date = new Date();
        base.addElement("TIDNO").addText(parmMap.get("tidno"));//不再使用统一的终端号，快钱、支付宝分开
        base.addElement("BK_ACCT_DATE").addText(new SimpleDateFormat("yyyyMMdd").format(date));
        base.addElement("BK_ACCT_TIME").addText(new SimpleDateFormat("HHmmss").format(date));
        base.addElement("BK_SERIAL").addText(parmMap.get("trade_no"));//空值
        base.addElement("BK_TRAN_CHNL").addText("WEB");
        base.addElement("PAY_APP_NO").addText(parmMap.get("pay_app_no"));
        base.addElement("AMOUNT").addText(parmMap.get("amount"));
        base.addElement("PAYCOUNT").addText("1");
        base.addElement("CHECK_OPCODE");
        base.addElement("CHECK_OPNAME");
        Element detail = body.addElement("DETAILS");
        Element payinfo = detail.addElement("PAYINFO");
        payinfo.addElement("BATCH_NO").addText(parmMap.get("batch_no"));
        payinfo.addElement("CASH_NO").addText(parmMap.get("cash_no"));
        payinfo.addElement("CARD_NO").addText(parmMap.get("card_no"));
        payinfo.addElement("SUB_AMOUNT").addText(parmMap.get("sub_amount"));
        return document.asXML();
    }

    /**
     * 解析调用支付平台支付确认返回报文
     *
     * @param responseXML
     * @return
     * @throws UnsupportedEncodingException
     * @throws DocumentException
     */
    public BankConsumeResponse parse_BankConsumeResponse(String responseXML)
            throws UnsupportedEncodingException, DocumentException {
        SAXReader reader = new SAXReader(false);// 初始化新的SAXReader
        BankConsumeResponse response = new BankConsumeResponse();
        InputStream in = new ByteArrayInputStream(responseXML.getBytes("UTF-8"));
        Document document = reader.read(in);
        Element root = document.getRootElement();
        Element head = root.element("HEAD");
        response.setTradeType(head.elementTextTrim("TRAN_CODE"));
        response.setRcpt_no(head.elementTextTrim("RCPT_NO"));
        response.setRslt_code(head.elementTextTrim("RSLT_CODE"));
        response.setRslt_msg(head.elementTextTrim("RSLT_MSG"));
        Element body = root.element("BODY");
        Element base = body.element("BASE");
        response.setPay_apply_no(base.elementTextTrim("PAY_APP_NO"));
        response.setCheck_code(base.elementTextTrim("CHECK_CODE"));
        response.setAmount(base.elementTextTrim("AMOUNT"));
        response.setRemark(base.elementTextTrim("REMARK"));
        return response;
    }

    /**
     * 将支付宝传过来的参数构建成调用支付平台的参数
     *
     * @param parmMap
     * @return
     */
    public Map<String, String> buildConsumeMapFOR_ALIPAY(
            Map<String, String> parmMap) {
        Map<String, String> parmMapTemp = new HashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date gmt_payment = null;
        try {
            gmt_payment = dateFormat.parse(parmMap.get("gmt_payment"));// 交易付款时间
            parmMapTemp.put("bk_acct_date",
                    new SimpleDateFormat("yyyyMMdd").format(gmt_payment));
            parmMapTemp.put("bk_acct_time",
                    new SimpleDateFormat("HHmmss").format(gmt_payment));
            parmMapTemp.put("trade_no", parmMap.get("trade_no"));// 支付宝交易号
            parmMapTemp.put("pay_app_no", parmMap.get("out_trade_no"));
            parmMapTemp.put("amount", new BigDecimal(parmMap.get("total_fee"))
                    .multiply(new BigDecimal(100)).stripTrailingZeros()
                    .toPlainString());// 交易金额
            parmMapTemp.put("batch_no", parmMap.get("trade_no"));
            parmMapTemp.put("cash_no", "000");
            parmMapTemp.put("card_no", parmMap.get("buyer_id"));
            parmMapTemp.put(
                    "sub_amount",
                    new BigDecimal(parmMap.get("total_fee"))
                            .multiply(new BigDecimal(100)).stripTrailingZeros()
                            .toPlainString());
            parmMapTemp.put("MIDNO", "2088021647143507");
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("[" + dateFormat.format(new Date())
                    + "]支付宝异步通知请求返回参数中支付申请号[" + parmMap.get("out_trade_no")
                    + "]通知时间转换异常[" + parmMap.get("notify_time") + "]");
        }
        return parmMapTemp;
    }

    /**
     * 将支付宝传过来的参数构建成调用支付平台的参数,手机版app
     *
     * @param parmMap
     * @return
     */
    public Map<String, String> buildConsumeMapFOR_APPALIPAY(Map<String, String> parmMap) {
        Map<String, String> parmMapTemp = new HashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date gmt_payment = null;
        try {
            gmt_payment = dateFormat.parse(parmMap.get("gmt_payment"));//交易付款时间y
            parmMapTemp.put("bk_acct_date", new SimpleDateFormat("yyyyMMdd").format(gmt_payment));//y
            parmMapTemp.put("bk_acct_time", new SimpleDateFormat("HHmmss").format(gmt_payment));//y
            parmMapTemp.put("trade_no", parmMap.get("trade_no"));//支付宝交易号y
            parmMapTemp.put("pay_app_no", parmMap.get("out_trade_no"));//y
            parmMapTemp.put("amount", new BigDecimal(parmMap.get("total_fee")).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());//交易金额//y
            parmMapTemp.put("batch_no", parmMap.get("trade_no"));//y
            parmMapTemp.put("cash_no", "000");//y
            parmMapTemp.put("card_no", parmMap.get("buyer_id"));//卖家支付宝用户号
            parmMapTemp.put("sub_amount", new BigDecimal(parmMap.get("total_fee")).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());//y
            parmMapTemp.put("MIDNO", "2088021647143507");
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("[" + dateFormat.format(new Date()) + "]手机app支付宝异步通知请求返回参数中支付申请号[" + parmMap.get("out_trade_no") + "]通知时间转换异常[" + parmMap.get("notify_time") + "]");
        }
        return parmMapTemp;
    }

    /**
     * 将银联支付完成传过来的参数构建成调用支付平台的参数
     *
     * @param paymentResult
     * @return
     */
    public Map<String, String> buildConsumeMapFOR_UnionPay(Map<String, String> paymentResult) {
        Map<String, String> parmMapTemp = new HashMap<String, String>();
        parmMapTemp.put("MIDNO", paymentResult.get("merId"));//商户号
        parmMapTemp.put("bk_acct_date", paymentResult.get("txnTime").substring(0, 8));//交易日期
        parmMapTemp.put("bk_acct_time", paymentResult.get("txnTime").substring(8, 14));//交易时间
        parmMapTemp.put("trade_no", paymentResult.get("traceNo"));//银联消费交易的流水号
        parmMapTemp.put("pay_app_no", paymentResult.get("orderId"));//订单号
        parmMapTemp.put("amount", paymentResult.get("txnAmt"));
        parmMapTemp.put("batch_no", paymentResult.get("queryId"));
        parmMapTemp.put("cash_no", "000");//
        parmMapTemp.put("card_no", "0");//测试赋值
        parmMapTemp.put("sub_amount", paymentResult.get("txnAmt"));

        return parmMapTemp;
    }

    /**
     * 根据投保单查询核心、中间业务平台是否产品保单号
     * 请求报文XML
     * @param plyAppNo
     * @param prodCode
     * @return
     */
    public  String build_QueryPlyNoRequest(String plyAppNo,String prodCode) {
        Document document = DocumentHelper.createDocument();
        Element packet= document.addElement("PACKET");
        Element head=packet.addElement("HEAD");
        Element body = packet.addElement("BODY");
        document.setXMLEncoding("UTF-8");
        if(prodCode.startsWith("03")){
            body.addElement("PLY_APP_NO").addText(plyAppNo);
        }else{
            head.addElement("TRANSTYPE").setText("SNY");
            head.addElement("SYSCODE").setText("HUAAN");
            head.addElement("TRANSCODE").setText("100003");
            head.addElement("CONTENTTYPE").setText("XML");
            head.addElement("VERIFYTYPE").setText("1");
            head.addElement("USER").setText("NETSALES");
            head.addElement("PASSWORD").setText("SINOSAFE95556");
            Element third = packet.addElement("THIRD");
            third.addElement("EXTENTERPCODE").setText("COMMONQUERY");
            third.addElement("PRODNO").setText("0000");
            third.addElement("PLANNO").setText("0000");
            third.addElement("TRANSCODE").setText("100003");
            body.addElement("C_PLY_APP_NO").setText(plyAppNo);
            body.addElement("C_PLY_NO").setText("H10601067420161105999");
        }
        logger.info("请求报文XML:"+document.asXML());
        return document.asXML();
    }

    /**
     * * 根据投保单查询核心、中间业务平台是否产品保单号
     * 解析返回报文XML封装对象
     * @param xml
     * @param prodCode
     * @return
     */
    public  BankConsumeResponse build_QueryPlyNoResponse(String xml,String prodCode) {
        logger.info("解析返回报文XML封装对象:"+xml);
        BankConsumeResponse response=new BankConsumeResponse();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root=document.getRootElement();
            if(prodCode.startsWith("03")){
                if(root.element("HEAD")!=null&& "0".equals(root.element("HEAD").elementText("RESPONSECODE"))){
                    String plyNo=root.element("BODY").element("DATA").elementText("PLY_NO");
                    response.setPlyNo(plyNo);
                }else{
                    String msg=root.element("HEAD").elementText("ERRORMESSAGE");
                    response.setRslt_code("9999");
                    response.setRslt_msg(msg);
                }
            }else{
                if(root.element("HEAD")!=null&& root.element("HEAD").elementText("ERRORMESSAGE")==null){
                    String plyNo=((Element)root.element("BODY").element("BASE_LIST").elements().get(0)).elementText("C_PLY_NO");
                    response.setPlyNo(plyNo);
                }else{
                    String msg=root.element("HEAD").elementText("ERRORMESSAGE");
                    response.setRslt_code("9999");
                    response.setRslt_msg(msg);
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
            response.setRslt_code("9999");
            response.setRslt_msg("解析保单查询接口报文异常"+e.getMessage());
        }
        return response;
    }
}
