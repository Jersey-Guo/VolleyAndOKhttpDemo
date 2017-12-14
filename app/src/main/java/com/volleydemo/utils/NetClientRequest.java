package com.volleydemo.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by guojiadong
 * on 2016/12/14.
 */

public class NetClientRequest extends JsonRequest<JSONObject> {
    /**
     * 超时时间
     */
    private static final int TIME_OUT = 500;

    public NetClientRequest(int method, String url,
                            Map<String, String> postParams, Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, paramstoString(postParams), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public NetClientRequest(String url, Map<String, String> params,
                            Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(Method.GET, paramsCastUrl(url, params), null, listener, errorListener);
    }


    public NetClientRequest(String url, Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        this(Method.GET, url, null, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

            JSONObject jsonObject = new JSONObject(new String(response.data,
                    "UTF-8"));

            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {

            return Response.error(new ParseError(e));

        }
    }

    /**
     * 把map参数 拼接成 get请求的 url格式 ,最后和 传过来的url一起拼接
     */
    public static String paramsCastUrl(String url, Map<String, String> map) {
        if (map != null) {
            String params = "?";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params += entry.getKey() + "=" + entry.getValue() + "&";
            }
            params = params.substring(0, params.length() - 1);
            return url + params;
        }
        return url;
    }

    // 拼接Post请求参数
    private static String paramstoString(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            String paramsEncoding = "UTF-8";
            StringBuilder encodedParams = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(),
                            paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(),
                            paramsEncoding));
                    encodedParams.append('&');

                }
                return encodedParams.toString();
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: "
                        + paramsEncoding, uee);
            }
        }
        return "";
    }
}
