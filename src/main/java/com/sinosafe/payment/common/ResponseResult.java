package com.sinosafe.payment.common;


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

    public final static String SUCESS="000000";

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
