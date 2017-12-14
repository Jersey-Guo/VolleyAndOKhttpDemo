package com.volleydemo.utils;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

import static com.volleydemo.BaseApplication.requestQueue;


/**
 * Created by guojiadong
 * on 2016/12/14.
 */

public abstract class HttpHelper<T> implements Response.Listener<JSONObject>,Response.ErrorListener {


    private Activity mActivity;

    private boolean isShowHint = true; // is show the hint message popupWindow

    public HttpHelper(Activity activity) {
        super();
        this.mActivity = activity;
    }

    protected Activity getContext() {
        return mActivity;

    }

    /**
     * 是否开启加载数据请求的提示框
     */
    public void closeShowHint() {
        isShowHint = false;

    }

    protected NetClientRequest getRequestForGet(String url,
                                                Map<String, String> params) {
        if (params == null) {
            return new NetClientRequest(url, this, this);
        } else {
            return new NetClientRequest(url, params, this, this);
        }

    }

    protected NetClientRequest getRequestForPost(String url,
                                               Map<String, String> params) {
        return new NetClientRequest(Request.Method.POST, url, params, this, this);
    }

    public void doHttpGet(String url, Map<String, String> params) {
        if (isShowHint) {
            // 弹出正在加载数据弹框
        }
        NetClientRequest requestForGet = getRequestForGet(url, params);
        requestForGet.setTag(paramsCastUrl(url, params));
        requestQueue.cancelAll(paramsCastUrl(url, params));
        requestQueue.add(requestForGet);

    }

    /**
     * get请求
     *
     * @param url
     */
    public void get(String url) {
        doHttpGet(url, null);
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

    /**
     * post请求
     *
     * @param url
     */
    public void doHttpPost(String url, Map<String, String> params) {
        requestQueue.add(getRequestForPost(url, params));
        Log.e("onVoequest-----",params.toString());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (isShowHint) {
        }
        showErrorMsg();
        disposeVolleyError(error);
    }

    protected abstract void disposeVolleyError(VolleyError error);

    @Override
    public void onResponse(JSONObject response) {

        if (isShowHint) {
        }
        if (response != null) {
            disposeResponse(response);
        } else {
            showErrorMsg();
        }

    }

    /**
     * 数据加载错误提示
     */
    protected void showErrorMsg() {

    }

    protected abstract void disposeResponse(JSONObject response);

    private UIDataListener<T> dataListener;

    public void setDataListener(UIDataListener<T> dataListener) {
        this.dataListener = dataListener;
    }

    protected void notifyDataChanged(T data) {
        if (dataListener != null) {
            dataListener.onDataChanged(data);
        }
    }

    protected void notifyErrorHappened() {
        if (dataListener != null) {
            dataListener.onErrorHappend("");
        }
    }
}
