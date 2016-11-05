package com.sinosafe.payment.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author liuying
 * @Description:Http请求工具类
 * @time:2015年4月29日 上午9:12:31
 */

public class HttpUtils {


    private HttpUtils() {


    }


    public static String doPost(String url, Map<String, String> data,

                                String charset) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {

            HttpPost httpPost = new HttpPost(url);

            // request相关设置

		/*	RequestConfig config = RequestConfig.custom()

					.setSocketTimeout(ParamConsts.SOCKETTIMEOUT) // 数据传输超时

					.build();

			httpPost.setConfig(config);*/

            if (data != null && !data.isEmpty()) {

                // 打包将要传入的参数

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                for (Iterator<Entry<String, String>> it = data.entrySet()

                        .iterator(); it.hasNext(); ) {

                    Entry<String, String> entry = it.next();

                    params.add(new BasicNameValuePair(entry.getKey(), entry

                            .getValue()));

                }

                if (charset != null)

                    httpPost.setEntity(new UrlEncodedFormEntity(params, charset));

                else

                    httpPost.setEntity(new UrlEncodedFormEntity(params));

            }

            if (StringUtils.isNotEmpty("")) {

                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            }

            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {

                // 响应状态码

                if (200 == response.getStatusLine().getStatusCode()) {

                    HttpEntity entity = response.getEntity();

                    if (entity != null) {

                        return EntityUtils.toString(entity, charset);

                    }

                } else {

                    throw new Exception("Response Code :"

                            + response.getStatusLine().getStatusCode());

                }


            } finally {

                response.close();

            }

        } finally {

            httpclient.close();

        }

        return null;

    }


    public static String doPost(String url, Map<String, String> data,

                                int socketTimeout, String charset, String contentType)

            throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {

            HttpPost httpPost = new HttpPost(url);

            // request相关设置

            RequestConfig config = RequestConfig.custom()

                    .setSocketTimeout(socketTimeout) // 数据传输超时

                    .build();

            httpPost.setConfig(config);

            if (data != null && !data.isEmpty()) {

                // 打包将要传入的参数

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                for (Iterator<Entry<String, String>> it = data.entrySet()

                        .iterator(); it.hasNext(); ) {

                    Entry<String, String> entry = it.next();

                    params.add(new BasicNameValuePair(entry.getKey(), entry

                            .getValue()));

                }

                if (charset != null)

                    httpPost.setEntity(new UrlEncodedFormEntity(params, charset));

                else

                    httpPost.setEntity(new UrlEncodedFormEntity(params));

            }

            if (StringUtils.isNotEmpty(contentType)) {

                httpPost.setHeader("Content-Type", contentType);

            }

            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {

                // 响应状态码

                if (200 == response.getStatusLine().getStatusCode()) {

                    HttpEntity entity = response.getEntity();

                    if (entity != null) {

                        return EntityUtils.toString(entity, charset);

                    }

                } else {

                    throw new Exception("Response Code :"

                            + response.getStatusLine().getStatusCode());

                }


            } finally {

                response.close();

            }

        } finally {

            httpclient.close();

        }

        return null;

    }


}

