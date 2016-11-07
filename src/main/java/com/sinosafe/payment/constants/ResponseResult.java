package com.sinosafe.payment.constants;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created with base.
 * User: anguszhu
 * Date: Dec,05 2015
 * Time: 6:44 PM
 * description:
 */
public class ResponseResult implements Serializable {

    public final static String FUIOU_SUCESS="0000";

    public final static String SUCCESS="0000";

    public final  static String PARA_MISS = "990100"; // 缺失参数
    public final  static String PARA_ERROR = "990101"; // 参数错误

    public final  static String PRODUCT_POINT_ERROR = "990200";  // 生成积分系统异常
    public final  static String PRODUCT_POINT_RULE_ERROR = "990201";  // 无此生成积分规则
    public final static String POINTS_SOURCE_ERROR = "990202";   //无此积分类型
    public final  static String PRODUCT_POINT_PAPA_REPEAT = "990203";  // 生成积分参数重复

    public final static String NO_POINT_SOURCE = "990300";//未选择系统类型

    public final static String NO_MECHANISM = "990301";//未选择机构

    public final static String POINT_SETTING_IS_HAVE = "990302";//积分规则已经存在

    public final static String ADD_POINT_SETTING_ERROR = "990303";//新增积分规则出错

    public final static String NO_SIGN = "990304";//用户未登陆

    public final static String UPDATE_POINT_SETTING_ERROR = "990305";//修改积分规则出错

    public final static String MISS_PARAM = "990306";//缺少参数

    public final static String MORE_PARAM = "990307";//多余的参数

    public final  static String PRODUCT_SETTING_ERROR = "990300";  // 规则生成异常

    public final static String FIND_RULE_ERROR = "990308";//根据条件查询积分规则出错

    public final static String FIND_RULE_BY_RULEID_ERROR = "990309";//根据积分规则ID查询积分规则出错

    public final static String STRAT_RULE_ERROR = "990310";//启用积分规则失败
    //支付异常相关
    public static final String TA_NO_INVALID = "980001";//taNo 不存在/或者找不到

    public static final String AMOUNT_INVALID = "980002" ;//支付额度或红包额度非法

    public static final String BANKID_INVALID = "980003";//无法找到银行列表ID

    public static final String CARD_HOLDER_INVALID = "980004" ; //持卡人姓名非法

    public static final String CARD_HOLDER_CERT_TYPE_INVALID = "980005" ; //持卡人姓名非法

    public static final String PRESAVE_MOBILE_INVALID = "980006" ; //预留手机号非法

    public static final String CREDIT_CARD_PARAM_INVALID = "980007" ; //信用卡信息非法

    public static final String TA_FINISHED ="980008" ; //已支付，请重复支付
    public static final String OFFER_AMOUT_CHANGED = "980009" ; //红包变更
    public static final String TOTAL_AMOUT_CHANGED = "980010"; //支付总金额变更
    public static final String CARD_NO_CHANGED = "980011"; //卡号变更
    public static final String PRESAVE_MOBILE_CHANGED = "980012"; //手机变更
    public static final String CHECK_CODE_INVAILD = "980013" ; //验证码非法
    public static final String QUICKPAY_SMS_FAILED =  "980014"; //短信发送失败
    public static final String PAY_RECORD_LOST = "980015" ; //支付数据丢失
    public static final String QUICKPAY_FAILED = "980016";  //快捷支付失败
    public static final String PRODUCE_POLICY_FAILED = "980017"; //出单失败
    public static final String PARSE_PRODUCE_XML_FAILED = "980018" ; //解析出单返回报文异常

    public static final String NO_CARD_BIN_MATCH = "980101" ; //卡bin匹配不到数据

    private String returnCode;
    private String errorMsg;
    private Object businessDto;

    public ResponseResult(String errorCode, String desc, Object object) {
            this.returnCode = errorCode;
            this.errorMsg = desc;
            this.businessDto =object;
    }

    @Override
    public String toString() {
        JSONObject o=new JSONObject();
        o.put("returnCode",this.returnCode);
        o.put("errorMsg",this.errorMsg);
        o.put("businessDto",this.businessDto);
        return o.toJSONString();
    }


    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getBusinessDto() {
        return businessDto;
    }

    public void setBusinessDto(Object businessDto) {
        this.businessDto = businessDto;
    }
}
