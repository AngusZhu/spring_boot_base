package com.sinosafe.payment.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.Map;

/**
 * Created by zhuhuanmin on 2016/4/23.
 */
public class RequestUtils {

    public static JSONObject transStringToJson(String transStr){
        Map<String, String> stringMap = Splitter.on("&").withKeyValueSeparator("=").split(transStr);
        return JSON.parseObject(JSON.toJSONString(stringMap, true));
    }



}
