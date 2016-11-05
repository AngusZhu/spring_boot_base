/*
 * Copyright (c) 2013 FNDSOFT Co.,Ltd. All rights reserved.
 */
package com.sinosafe.payment.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sinosafe.payment.bo.InterfaceLog;
import com.sinosafe.payment.excepiton.*;
import com.sinosafe.payment.service.pay.wx.WxPayHelper;
import com.sinosafe.payment.vo.BankGateWay;
import com.sinosafe.payment.vo.RecvMerchantInfoResponse;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import  com.sinosafe.payment.vo.BankQueryResponse;

/**
 * <doc>支付网关验证类<doc>
 *
 * @author chensf
 * @version 1.0 2013-8-18 下午10:08:17
 * @since 1.0
 */

public class VerifyUtils {
    private static final Logger logger = Logger.getLogger(VerifyUtils.class);

    /**
     * 用户请求参数进行校验
     */
    public static void verifyRequestParm(Map<String, String> parmMap,
                                         InterfaceLog tradeLog)throws InvalidUserException, ValidateDataException {
        String userid = parmMap.get("partnerID");
        String orderId = parmMap.get("orderId");
        String checkCode = parmMap.get("checkCode");
        String trade_code = parmMap.get("trade_code");
        String verify_field = parmMap.get("verify_field");
        String orderAmount = parmMap.get("orderAmount");
        String rslt_msg;

        if (!StringUtils.hasText(userid)) {
            rslt_msg = "对不起,请求的用户信息无效,交易不能进行(系统流水号" + tradeLog.getTaNo() + ").";
            tradeLog.setPayFailCode("E01004");
            tradeLog.setPayFailReason(rslt_msg);
            throw new InvalidUserException(rslt_msg);
        }
        if (!StringUtils.hasText(orderId)) {
            rslt_msg = "对不起,支付申请号不能为空,交易不能进行(系统交易流水号" + tradeLog.getTaNo()
                    + ").";
            tradeLog.setPayFailCode("E03000");
            tradeLog.setPayFailReason(rslt_msg);
            throw new ValidateDataException(rslt_msg);
        }
//        if (!StringUtils.hasText(checkCode)) {
//            rslt_msg = "对不起,校验码不能为空,交易不能进行(系统交易流水号" + tradeLog.getTaNo() + ").";
//            tradeLog.setPayFailCode("E03006");
//            tradeLog.setPayFailReason(rslt_msg);
//            throw new ValidateDataException(rslt_msg);
//        }
        if (!StringUtils.hasText(trade_code)) {
            rslt_msg = "对不起,交易类型不能为空,交易不能进行(系统交易流水号" + tradeLog.getTaNo()
                    + ").";
            tradeLog.setPayFailCode("E03007");
            tradeLog.setPayFailReason(rslt_msg);
            throw new ValidateDataException(rslt_msg);
        }
        if (!StringUtils.hasText(verify_field)) {
            rslt_msg = "对不起,校验域不能为空,交易不能进行(系统交易流水号" + tradeLog.getTaNo() + ").";
            tradeLog.setPayFailCode("E03008");
            tradeLog.setPayFailReason(rslt_msg);
            throw new ValidateDataException(rslt_msg);
        }
        if (!StringUtils.hasText(orderAmount)) {
            rslt_msg = "对不起,订单金额不能为空,交易不能进行(系统交易流水号" + tradeLog.getTaNo()
                    + ").";
            tradeLog.setPayFailCode("E03009");
            tradeLog.setPayFailReason(rslt_msg);
            throw new ValidateDataException(rslt_msg);
        }
        try {
            new BigDecimal(orderAmount);
        } catch (Exception e) {
            rslt_msg = "对不起,订单金额异常,交易不能进行(系统交易流水号" + tradeLog.getTaNo() + ").";
            tradeLog.setPayFailCode("E03003");
            tradeLog.setPayFailReason(rslt_msg);
            throw new ValidateDataException(rslt_msg);
        }
    }


    /**
     * 获得收款申请请求后进行相应的验证
     *
     * @return
     * @throws ValidateDataException
     */
    public static boolean verify_Amount(String appAmount, String requestAmount,
                                        InterfaceLog tradeLog, RecvMerchantInfoResponse rmir)
            throws ValidateDataException {
        boolean verifyResult = false;
        String rslt_msg;
        if (new BigDecimal(requestAmount).multiply(new BigDecimal(100))
                .compareTo(new BigDecimal(appAmount)) == 0) {
            verifyResult = true;
        } else {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"]请求收款金额和系统已记录金额不相同,交易不能进行(交易号"+tradeLog.getTaNo()+").";W01006
            rslt_msg = "对不起,支付申请号[" + tradeLog.getTaNo()
                    + "]请求收款金额和系统已记录金额不相同,交易不能进行(交易号" + tradeLog.getTaNo()
                    + ").";
            tradeLog.setPayFailCode("W01006");
            tradeLog.setPayFailReason(rslt_msg);
            rmir.setRcpt_no(tradeLog.getTaNo());// 将E0对应的交易号保存下来
            throw new ValidateDataException(rslt_msg);
        }
        return verifyResult;
    }


    /**
     * 验证支付平台返回支付信息
     *
     * @param response
     * @param tradeLog
     * @throws BankResponseException
     */
    public static void verify_payment_Response(BankQueryResponse response,
                                               InterfaceLog tradeLog) throws BankResponseException {
        String rslt_msg;
        if ("000000".equals(response.getRslt_code())) {
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(response.getRslt_msg());
        } else if ("400002".equals(response.getRslt_code())
                || "400007".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"]已晚于最迟缴费时间,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W01003
            rslt_msg = Basic2Config.errorInfo("W01003", Basic2Config.W01003,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300009".equals(response.getRslt_code())) {
            // rslt_msg="支付方式不正确 （web和pos）.";
            rslt_msg = Basic2Config.errorInfo("W03001", Basic2Config.W03001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W03001
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300008".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"],校验码无效,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W03002", Basic2Config.W03002,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E01002
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300007".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"],校验码无效,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W05002", Basic2Config.W05002,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E01002
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300006".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"],校验码无效,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W05001", Basic2Config.W05001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E01002
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300005".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"],校验码无效,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("E01002", Basic2Config.E01002,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E01002
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300004".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"]当前已被作废,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W01004", Basic2Config.W01004,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W01004
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300002".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"]已消费完成,不能重复支付(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W01005", Basic2Config.W01005,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W01005
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("300001".equals(response.getRslt_code())) {
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"],交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("W01001", Basic2Config.W01001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W01001
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("200002".equals(response.getRslt_code())) {
            rslt_msg = Basic2Config.errorInfo("E02002", Basic2Config.E02002,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg="对不起,该用户没用对应的处理权限,支付申请号["+tradeLog.getApplyNo()+"],交易不能进行(交易号"+tradeLog.getTaNo()+").";
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E02002
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("200001".equals(response.getRslt_code())) {
            // rslt_msg="对不起,该用户无效，不能进行支付查询.";
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E02001
            rslt_msg = Basic2Config.errorInfo("E02001", Basic2Config.E02001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("200003".equals(response.getRslt_code())) {
            // rslt_msg="对不起,该终端没用对应的处理权限,支付申请号["+tradeLog.getApplyNo()+"],交易不能进行(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("E04001", Basic2Config.E04001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();E04001
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("400001".equals(response.getRslt_code())) {// 业务数据错误

            rslt_msg = Basic2Config.errorInfo("W01007", Basic2Config.W01007,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else if ("900001".equals(response.getRslt_code())) {
            // rslt_msg="对不起,当前系统忙,请稍后再试(交易号"+tradeLog.getTaNo()+").";
            rslt_msg = Basic2Config.errorInfo("F01001", Basic2Config.F01001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();F01001
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        } else {
            rslt_msg = Basic2Config.errorInfo("W01001", Basic2Config.W01001,
                    tradeLog.getApplyNo(), tradeLog.getTaNo());
            // rslt_msg="对不起,支付申请号["+tradeLog.getApplyNo()+"]进行支付查询时校验不通过,交易不能进行(交易号"+tradeLog.getTaNo()+").";
            // rslt_msg+="\r\n具体错误信息："+response.getRslt_msg();W01001
            tradeLog.setPayFailCode(response.getRslt_code());
            tradeLog.setPayFailReason(rslt_msg);
            throw new BankResponseException(rslt_msg);
        }
    }

    /**
     * 对微信支付完成返回的数据进行签名验证
     *
     * @param parmMap
     * @param gateWay
     * @return
     */
    public static boolean verify_WX_Response(Map<String, String> parmMap,
                                             BankGateWay gateWay) {
        boolean result = false;
        HashMap<String, String> nativeObj = new HashMap<String, String>();
        // 将返回的map中key为sign去掉，
        for (String key : parmMap.keySet()) {
            String value = parmMap.get(key);
            if ("sign".equals(key)) {
                continue;
            }
            nativeObj.put(key, value);
        }
        // 新生成的签名
        String sign = "";
        try {
            sign = WxPayHelper.GetBizSign(nativeObj, gateWay.getSignKey());
        } catch (SDKRuntimeException e) {
            e.printStackTrace();
        }
        if (sign.equals(parmMap.get("sign"))) {
            return true;
        }
        return result;
    }

    /**
     * 支付支付宝返回参数进行验证
     *
     * @param params
     * @param gateWay
     * @return
     */
    public static boolean verify_ALIPAY_Response(Map<String, String> params,
                                                 BankGateWay gateWay) {
        String mysign = GateWayCore.getMysign(params, gateWay.getSignKey(),
                gateWay.getCharset());
        if (logger.isDebugEnabled()) {
            logger.info("支付宝请求参数签名字符串[" + mysign + "]");
            logger.info("支付宝参数中的签名字符串[" + params.get("sign") + "]");
        }
        String veryfy_url = gateWay.getVeryfyUrl() + "partner="
                + gateWay.getRequestId() + "&notify_id="
                + params.get("notify_id");
        System.out.println("params.get(\"sign\").equals(mysign):"
                + params.get("sign").equals(mysign));
        logger.error("params.get(\"sign\").equals(mysign):"
                + params.get("sign").equals(mysign));
        return (params.get("sign").equals(mysign))
                && (checkUrlFor_ALIPAY(veryfy_url).equals("true"));
    }

    /**
     * 使用支付宝返回参数调用支付宝服务进行签名验证
     *
     * @param urlvalue
     * @return
     */
    private static String checkUrlFor_ALIPAY(String urlvalue) {
        String inputLine = "";
        SimpleDateFormat logTimeFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            logger.error("[" + logTimeFormat.format(new Date())
                    + "]开始调用支付宝验证签名参数,地址[" + urlvalue + "]");
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
            logger.error("[" + logTimeFormat.format(new Date())
                    + "]支付宝通知参数回调合法性验证结果[" + inputLine.toUpperCase() + "]");
        } catch (Exception e) {
            logger.error("支付宝服务验证失败");
            logger.error(e.getMessage());
            e.printStackTrace();
            inputLine = "";
        }
        System.out.println("支付宝服务验证结果:" + inputLine);
        logger.error("支付宝服务验证结果:" + inputLine);
        return inputLine;
    }

}
