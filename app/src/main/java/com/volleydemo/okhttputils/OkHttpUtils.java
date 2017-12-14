package com.volleydemo.okhttputils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by guojiadong
 * on 2016/12/15.
 */

public class OkHttpUtils {

    public OkHttpClient mClient;
    public Handler mHandler;
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    /**
     * 单例模式
     */
    private static class InstanceHolder {
        public static OkHttpUtils INSTANCE = new OkHttpUtils();
    }

    public static OkHttpUtils getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public OkHttpUtils() {
        mClient = new OkHttpClient();
        /**
         * 在这里直接设置连接超时.读取超时，写入超时
         */
        mClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
        mClient.newBuilder().readTimeout(10, TimeUnit.SECONDS);
        mClient.newBuilder().writeTimeout(10, TimeUnit.SECONDS);
        /**
         * 初始化handler
         */
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 处理参数
     *
     * @param map
     * @return
     */
    private RequestBody getRequestBody(Map<String, String> map) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder.build();
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
     * get请求
     */
    public void get(Context context, String url, Map<String, String> map, DataCallBack callBack) {
        if (context == null) {
            return;
        }
        Request request = addHeaders().url(paramsCastUrl(url, map)).get().build();
        clientCall(callBack, request);
    }

    /**
     * post请求
     */
    public void post(Context context, String url, Map<String, String> map, DataCallBack callBack) {
        if (context == null) {
            return;
        }
        Request request = addHeaders().url(url).post(getRequestBody(map)).build();
        clientCall(callBack, request);
    }

    /**
     * 提交文件
     * @param context
     * @param url 请求URL
     * @param map 请求参数
     * @param filePath 上传文件地址
     * @param callBack 结果回调
     */
    public void uploadFile(Context context, String url, Map<String, String> map, String filePath, DataCallBack callBack) {
        if (context == null) {
            return;
        }
        File file = new File(filePath);
        Request request = addHeaders().url(url).post(getRequestBody(map).create(MEDIA_TYPE_MARKDOWN, file)).build();
        clientCall(callBack, request);
    }

    /**
     * 统一添加请求
     *
     * @param callBack 响应回调
     * @param request
     */
    private void clientCall(final DataCallBack callBack, Request request) {
        if (callBack != null) {
            callBack.onStart();
        }
        Log.e("onRequest----", request.toString() + request.method());
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                failedCallBack(e.getMessage(), callBack);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    successCallBack(response.body().string(), callBack);
                } else {
                    failedCallBack(response.message(), callBack);
                }
            }
        });
    }

    /**
     * 统一处理成功信息
     *
     * @param result
     * @param callBack
     */
    private void successCallBack(final String result, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     */
    private void failedCallBack(final String errorMsg, final DataCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(errorMsg);
                }
            }
        });
    }


    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder();
//                .addHeader("Connection", "keep-alive")
//                .addHeader("platform", "2")
//                .addHeader("phoneModel", Build.MODEL)
//                .addHeader("systemVersion", Build.VERSION.RELEASE)
//                .addHeader("appVersion", "3.2.0");
        return builder;
    }



    public interface DataCallBack {
        /**
         * 开始
         */
        void onStart();

        /**
         * 成功
         *
         * @param result 成功后返回值，一般为json
         */
        void onSuccess(String result);

        /**
         * 失败
         */
        void onFailure(String error);
    }
}
